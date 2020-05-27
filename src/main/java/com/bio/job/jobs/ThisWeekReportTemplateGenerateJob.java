package com.bio.job.jobs;

import java.text.SimpleDateFormat;
import java.util.*;

import com.bio.sys.domain.*;
import com.bio.sys.service.*;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bio.common.domain.MailBean;
import com.bio.common.utils.DateUtils;
import com.bio.common.utils.ThreadPool;

/**
 * 
 * 每周一早七点执行
 * 
 * @author chenx
 *
 */
@Component
public class ThisWeekReportTemplateGenerateJob implements Job {

    private Logger logger = LoggerFactory.getLogger(MailService.class);

    @Value("${bio.projectRootURL}")
    private String url;
    
	@Autowired
	private UserService userService;

	@Autowired
	private DeptService deptService;

	@Autowired
	private RoleService userRoleService;

	@Autowired
	private ReportService reportService;

	@Autowired
	private ReportContentService reportContentService;

	@Autowired
	private SummaryService summaryService;
	@Autowired
	private UserPlanService userPlanService;
	
	@Autowired
	private MailService mailService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Map<String, Object> columnMap = new HashMap<>();
		columnMap.put("status", "1"); // 正常用户
		List<UserDO> users = userService.selectByMap(columnMap);

		List<ReportDO> reportDOs = new ArrayList<ReportDO>();
		List<ReportContentDO> reportContentDOS = new ArrayList<>();
		List<SummaryDO> summaryDOs = new ArrayList<SummaryDO>();

		// Step 0，准备数据
		for (UserDO userDO : users) {
			Long roleDOs = userRoleService.findRoleIdByUserId(userDO.getId());

			if (null != roleDOs && (roleDOs.intValue()!=1&&roleDOs.intValue()!=5)) { // 除管理员，陈老师外其他人都需要填写周报
				ReportDO reportDO = new ReportDO();
				reportDO.setAuthorId(userDO.getId());
				reportDO.setStatusMod(0); // 标识 Bot 生成的
				reportDO.setAuthorName(userDO.getName());

				//获取上周的计划(属于上周周报内容里的下周计划)
				String lastPlan="";
				ReportDO lastReportDO = reportService.getLatestReport(userDO.getId(),new Date());
				if(lastReportDO!=null)
					lastPlan=reportContentService.getByUUID(lastReportDO.getContentId()).getNextPlan();


				//表示Bot生成的周报内容为空
				String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
				ReportContentDO reportContent = new ReportContentDO(uuid, lastPlan , "", "", "");

				UserPlanDO userPlan=userPlanService.getByAuthorId(userDO.getId());
				if(userPlan==null)
					userPlan=new UserPlanDO();
				reportContent.setMonthPlan(userPlan.getMonthPlan());
				reportContentDOS.add(reportContent);
				reportDO.setContentId(uuid);
				Date mon = DateUtils.getThisWeekMondayStart(new Date());
				Date sun = DateUtils.getThisWeekSundayEnd(new Date());
				String deptName = deptService.selectById(userDO.getDeptId()).getName();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				String title = sdf.format(mon) + "-" + sdf.format(sun) + "-" + deptName + "-" + userDO.getName()
						+ " 周报";

				if (reportService.getReportCountByTitle(title) > 0) {
					continue;
				}

				reportDO.setRFromDate(mon);
				reportDO.setRToDate(sun);
				reportDO.setRCreateDate(new Date());
				reportDO.setDeptId(userDO.getDeptId());

				reportDO.setDeptName(deptName);

				reportDO.setParentDeptId(0L);
				reportDO.setTitle(title);
				reportDO.setScore("90");
				reportDOs.add(reportDO);
			}

			if (null != roleDOs && roleDOs.intValue() == 2) { // 专题组长
				/*SummaryDO summaryDO = new SummaryDO();

				Date mon = DateUtils.getThisWeekMondayStart(new Date());
				Date sun = DateUtils.getThisWeekSundayEnd(new Date());
				summaryDO.setRFromDate(mon);
				summaryDO.setRToDate(sun);
				summaryDO.setRCreateDate(new Date());

				summaryDO.setDeptId(userDO.getDeptId());
				String deptName = deptService.selectById(userDO.getDeptId()).getName();
				summaryDO.setDeptName(deptName);
				summaryDO.setCount(0L);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				String title = sdf.format(mon) + "-" + sdf.format(sun) + "-" + deptName + " 周报";

				if (summaryService.getSummaryCountByTitle(title) > 0) {
					continue;
				}
				summaryDO.setTitle(title);

				summaryDOs.add(summaryDO);*/
			}
		}
		// Step 1, 批量插入周报
		if (reportDOs.size() > 0) {
			boolean b = reportService.insertBatch(reportDOs);
			boolean c = reportContentService.insertBatch(reportContentDOS);
			if (b&&c) {
				for (ReportDO reportDO : reportDOs) {
					ThreadPool.getThreadPool().addRunnable(new ThreadPool.Runnable() {
						@Override
						public void run() {
							try {
								MailBean mailBean = new MailBean();

								String recipient=  userService.selectById(reportDO.getAuthorId()).getEmail();
								mailBean.setSubject("【BioDashboard】本周的周报草稿已经为您生成！");
								mailBean.setRecipient(recipient);

								Map<String, Object> parameters = new HashMap<>();
								parameters.put("name", reportDO.getAuthorName());
								parameters.put("url", url);

								mailService.sendTemplateMail(mailBean, "newreport.html", parameters);
								
							} catch (Exception e) {
								e.printStackTrace();
					            logger.error("邮件发送失败", e.getMessage());
							}
						}
					});
				}
			}

			// Step 2,批量插入周报Summary
		/*	if (summaryDOs.size() > 0) {
				summaryService.insertBatch(summaryDOs);
			}*/

			System.out.println("生成周报任务及周报汇总任务执行 | " + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN_19));
		}
	}
}
