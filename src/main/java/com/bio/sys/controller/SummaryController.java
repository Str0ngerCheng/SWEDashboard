package com.bio.sys.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import com.bio.common.annotation.Log;
import com.bio.common.domain.MailBean;
import com.bio.common.utils.DateUtils;
import com.bio.sys.dao.DeptDao;
import com.bio.sys.domain.*;
import com.bio.sys.service.*;
import com.bio.sys.vo.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.bio.common.service.ContextService;
import com.bio.common.utils.Result;

/**
 *
 * <pre>
 * 周报汇总
 * </pre>
 * <small> 2019-12-18 15:03:07 | chenx</small>
 *
 */

@Controller
@RequestMapping("/bio/summary")
public class SummaryController {
	@Value("${bio.projectRootURL}")
	private String url;

	private Logger logger = LoggerFactory.getLogger(MailService.class);

	@Autowired
	private ReportService reportService;

	@Autowired
	private ReportContentService reportContentService;

	@Autowired
	private SummaryService summaryService;

	@Autowired
	private DeptService deptService;

	@Autowired
	private UserService userService;

	@Autowired
	private MailService mailService;



	@GetMapping()
	@RequiresPermissions("bio:summary:summary")
	String Summary(){
		return "bio/summary/summary";
	}

	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("bio:summary:summary")
	public Result<List<SummaryVO>> list(){

		List<SummaryDO> summaryDOList=summaryService.getThisWeekSummary();
		List<SummaryVO> summaryVOList=new ArrayList<>();
		for(SummaryDO summaryDO:summaryDOList){
			DeptDO deptDO=deptService.selectById(summaryDO.getDeptId());
			SummaryVO summaryVO=new SummaryVO(summaryDO,deptDO.getOrderNum());
			summaryVOList.add(summaryVO);
		}
		return Result.ok(summaryVOList);
	}

	//获取本周的专题周报信息
	@GetMapping("/weekInfo")
	@RequiresPermissions("bio:summary:summary")
	public String getWeekInfo(Model model){
		List<DeptDO> deptDOList=deptService.selectList(null);
		List<TopicReportStatisticVO> topicReportStatisticList = new ArrayList<>();
		List<TopicReportDetailsVO> topicReportDetailsList = new ArrayList<>();
		for(DeptDO deptDO:deptDOList) {
			if(deptDO.getId()!=0) {//根节点排除
				Long deptId = deptDO.getId();
				String deptName = deptDO.getName();
				Integer deptOrder=deptDO.getOrderNum();

				TopicReportStatisticVO topicReportStatistic = new TopicReportStatisticVO();
				topicReportStatistic.setDeptName(deptName);
				topicReportStatistic.setOrderNum(deptOrder);
				//查看该专题周报汇总是否提交
				if(!summaryService.getThisWeekSummaryByDeptId(deptId)) {
					topicReportStatistic.setIsLSubmit(0);
				}
				else {
					List<ReportDO> unMSubmitReportList = reportService.getThisWeekReportByDeptAndStatusMSub(deptId, 0);
					List<ReportDO> mSubmitReportList = reportService.getThisWeekReportByDeptAndStatusMSub(deptId, 1);
					/*获取专题周报统计信息*/
					int totalCount = unMSubmitReportList.size() + mSubmitReportList.size();
					int unMSubmitCount = unMSubmitReportList.size();
					String unMSubmitUserList = "";
					for (int i = 0; i < unMSubmitReportList.size(); i++) {
						if (i < unMSubmitReportList.size() - 1)
							unMSubmitUserList += unMSubmitReportList.get(i).getAuthorName() + "，";
						else unMSubmitUserList += unMSubmitReportList.get(i).getAuthorName();
					}
					topicReportStatistic.setTotalCount(totalCount);
					topicReportStatistic.setUnMSubmitCount(unMSubmitCount);
					topicReportStatistic.setUnMSubmitUsers(unMSubmitUserList);
					topicReportStatistic.setIsLSubmit(1);

					/*获取专题周报详细信息*/
					for (int i = 0; i < mSubmitReportList.size(); i++) {
						TopicReportDetailsVO topicReportDetails = new TopicReportDetailsVO();
						ReportDO report = mSubmitReportList.get(i);
						topicReportDetails.setDeptName(deptName);
						//设置排序因子
						topicReportDetails.setDeptOrder(deptOrder);
						topicReportDetails.setUserOrder(userService.selectById(report.getAuthorId()).getOrderNum());

						topicReportDetails.setAuthorName(report.getAuthorName());
						ReportContentDO reportContent = reportContentService.getByUUID(report.getContentId());
						topicReportDetails.setMonthPlan(reportContent.getMonthPlan());
						topicReportDetails.setSummary(reportContent.getSummary());
						topicReportDetails.setNextPlan(reportContent.getNextPlan());
						topicReportDetails.setProblem(reportContent.getProblem());
						topicReportDetails.setComment(report.getComment());
						topicReportDetailsList.add(topicReportDetails);
					}

				}
				topicReportStatisticList.add(topicReportStatistic);
				Collections.sort(topicReportStatisticList);//这里直接在后端排序
				Collections.sort(topicReportDetailsList);//这里直接在后端排序
			}
		}
		model.addAttribute("topicReportStatisticList", topicReportStatisticList);
		model.addAttribute("topicReportDetailsList", topicReportDetailsList);
		Date mon = DateUtils.getThisWeekMondayStart(new Date());
		Date sun = DateUtils.getThisWeekSundayEnd(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String title = sdf.format(mon) + "-" + sdf.format(sun) + "-" + "SWE小组工作汇总";
		model.addAttribute("title", title);
		return "bio/summary/weekInfo";
	}


	@GetMapping("/add")
	@RequiresPermissions("bio:summary:add")
	String add(){
		return "bio/summary/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("bio:summary:edit")
	String edit(@PathVariable("id") Long id,Model model){
		SummaryDO summary = summaryService.selectById(id);
		model.addAttribute("summary", summary);
		return "bio/summary/edit";
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("bio:summary:add")
	public Result<String> save( SummaryDO summary){
		if(summaryService.insert(summary)){
			return Result.ok();
		}
		return Result.fail();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("bio:summary:edit")
	public Result<String>  update( SummaryDO summary){
		summaryService.updateById(summary);
		return Result.ok();
	}

	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("bio:summary:remove")
	public Result<String>  remove( Long id){
		if(summaryService.deleteById(id)){
			return Result.ok();
		}
		return Result.fail();
	}

	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("bio:summary:batchRemove")
	public Result<String>  remove(@RequestParam("ids[]") Long[] ids){
		summaryService.deleteBatchIds(Arrays.asList(ids));
		return Result.ok();
	}


	@ResponseBody
	@GetMapping("/submit")
	@RequiresPermissions("bio:summary:summary")
	public Result<String> submit(){
		List<DeptDO> deptDOList=deptService.selectList(null);
		//判断是否已经提交给小组负责人过
		List<SummaryDO>summaryDOList=summaryService.getThisWeekSummary();
		for(SummaryDO summaryDO:summaryDOList) {
			if (summaryDO.getStatus() == 1)
				return Result.build(2, "小组周报汇总已提交");
		}
		//查找是否有专题还未上交
		String unSubmitTopicList="";
		for(DeptDO deptDO:deptDOList) {
			if (deptDO.getId() != 0) {//根节点排除
				Long deptId = deptDO.getId();
				String deptName = deptDO.getName();
				if (!summaryService.getThisWeekSummaryByDeptId(deptId))
					unSubmitTopicList += deptName + "，";
			}
		}
		if(unSubmitTopicList=="")
			return Result.build(1,"有专题未提交周报汇总",unSubmitTopicList);
		else {
			MailBean mailBean = new MailBean();
			List<UserDO> userDOList = userService.getUsersByRoleId(5L);
			try {
				for(UserDO userDO:userDOList) {
					String recipient = userDO.getEmail();
					mailBean.setSubject("【SWEDashboard】本周的swe小组周报已提交！");
					mailBean.setRecipient(recipient);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("name", userDO.getName());
					parameters.put("url", url);
					mailService.sendAttachmentMail(mailBean, "summaryreport.html", parameters);
				}

				//将专题汇总周报状态设置为1
				List<SummaryDO>summaryDOList1=new ArrayList<>();
				for(SummaryDO summaryDO:summaryDOList) {
					summaryDO.setStatus(1);
					summaryDOList1.add(summaryDO);
				}
				summaryService.updateBatchById(summaryDOList1);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("邮件发送失败", e.getMessage());
				return Result.build(3,"邮件发送失败："+e.getMessage());
			}
			return Result.ok();
		}
	}


	@GetMapping("/meet")
	@RequiresPermissions("bio:summary:meet")
	public String meet(Model model){
		List<DeptDO> deptDOList=deptService.selectList(null);
		List<TopicReportDetailsVO> topicReportDetailsList = new ArrayList<>();
		for(DeptDO deptDO:deptDOList) {
			if(deptDO.getId()!=0) {//根节点排除
				Long deptId = deptDO.getId();
				String deptName = deptDO.getName();
				Integer deptOrder=deptDO.getOrderNum();

				//查看该专题周报汇总是否提交
				if(summaryService.getThisWeekSummaryByDeptId(deptId)) {
					List<ReportDO> reportList = reportService.getThisWeekReportByDeptAndStatusMSub(deptId, 1);
					/*获取专题周报详细信息*/
					for (ReportDO report:reportList) {
						TopicReportDetailsVO topicReportDetails = new TopicReportDetailsVO();
						topicReportDetails.setReportId(report.getId());
						topicReportDetails.setDeptName(deptName);
						//设置排序因子
						topicReportDetails.setDeptOrder(deptOrder);
						topicReportDetails.setUserOrder(userService.selectById(report.getAuthorId()).getOrderNum());
						topicReportDetails.setAuthorName(report.getAuthorName());

						ReportContentDO reportContent = reportContentService.getByUUID(report.getContentId());
						topicReportDetails.setMonthPlan(reportContent.getMonthPlan());
						topicReportDetails.setSummary(reportContent.getSummary());
						topicReportDetails.setNextPlan(reportContent.getNextPlan());
						topicReportDetails.setProblem(reportContent.getProblem());
						topicReportDetails.setComment(report.getComment());
						topicReportDetails.setSuggest(report.getSuggest());
						topicReportDetailsList.add(topicReportDetails);
					}
				}
				Collections.sort(topicReportDetailsList);//这里直接在后端排序
			}
		}
		model.addAttribute("topicReportDetailsList", topicReportDetailsList);
		Date mon = DateUtils.getThisWeekMondayStart(new Date());
		Date sun = DateUtils.getThisWeekSundayEnd(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String title = sdf.format(mon) + "-" + sdf.format(sun) + "-" + "SWE小组工作汇总";
		model.addAttribute("title", title);
		return "bio/summary/meet";
	}

	@RequiresPermissions("bio:summary:meet")
	@PostMapping("/submitSuggestion")
	@ResponseBody
	Result<String> submitSuggestion(@RequestBody List<TopicReportDetailsVO> topicReportDetailsVOList) {
		System.out.println(topicReportDetailsVOList.size());
		List<ReportDO> reportDOList=new ArrayList<>();
		for(TopicReportDetailsVO topicReportDetailsVO:topicReportDetailsVOList){
			ReportDO reportDO=new ReportDO();
			reportDO.setId(topicReportDetailsVO.getReportId());
			reportDO.setSuggest(topicReportDetailsVO.getSuggest());
			reportDOList.add(reportDO);
			System.out.println(reportDO.getSuggest());
		}
		reportService.updateBatch(reportDOList);
		return Result.ok();
	}

	@RequiresPermissions("bio:summary:chart")
	@GetMapping("/chart")
	String chart() {
		return "bio/summary/chart";
	}

	@ResponseBody
	@RequiresPermissions("bio:summary:chart")
	@GetMapping("/scoreChart")
	Result<List<ReportScoreVO>> scoreChart() {
		List<ReportScoreVO> reportScoreVOList=reportService.getMonthAVGReportScore();
		Collections.sort(reportScoreVOList);
		return Result.ok(reportScoreVOList);
	}

}
