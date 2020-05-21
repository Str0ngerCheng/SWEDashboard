package com.bio.sys.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.bio.common.annotation.Log;
import com.bio.common.service.ContextService;
import com.bio.common.utils.DateUtils;
import com.bio.common.utils.PageUtil;
import com.bio.common.utils.Result;
import com.bio.sys.domain.*;
import com.bio.sys.service.*;
import com.bio.sys.vo.ReportVO;
import com.bio.sys.vo.TopicReportDetailsVO;
import com.bio.sys.vo.TopicReportStatisticVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/bio/topic")
public class TopicController {
    @Autowired
    private ReportService reportService;
    @Autowired
    private ReportContentService reportContentService;
    @Autowired
    private SummaryService summaryService;
    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private ContextService contextService;

    @GetMapping()
    @RequiresPermissions("bio:topic:topic")
    public String Topic(){
        return "bio/topic/topic";
    }

    @ResponseBody
    @GetMapping("/weekList/{statusLSub}")
    @RequiresPermissions("bio:topic:topic")
    public Result<List<ReportVO>> geWeekList(@PathVariable("statusLSub")Integer statusLSub){
        UserDO userDO =  contextService.getCurrentLoginUser(SecurityUtils.getSubject());
        if(userDO!=null){
            List<ReportDO> reportDOtList=reportService.getThisWeekReportByDeptAndStatusLSub(userDO.getDeptId(),statusLSub);
            List<ReportVO> reportVOList=new ArrayList<>();
            for(int i=0;i<reportDOtList.size();i++){
                ReportDO reportDO=reportDOtList.get(i);
                UserDO user=userService.selectById(reportDO.getAuthorId());
                ReportVO reportVO=new ReportVO(reportDO,user.getOrderNum());
                reportVOList.add(reportVO);
            }
            Collections.sort(reportVOList);
            /*分页逻辑改成了前端分页（因为数据较少，所以可以一次加载且为了前端给所有user排序的需要,后端分页的话如果一页数据没有加载完
            所有user，则无法给所有人排序）*/
            //Page<ReportVO> page = PageUtil.getPage(pageNumber,pageSize,reportVOList);
            return Result.ok(reportVOList);
        }
        return Result.fail();
    }

    //获取本周的专题周报信息
    @GetMapping("/weekInfo/{deptId}")
    @RequiresPermissions("bio:topic:topic")
    public String getWeekInfo(@PathVariable("deptId")Long deptId,Model model){
        if(deptId==-1) {//-1在此表示专题负责人查看本专题信息，deptId从登录信息获取
            UserDO userDO = contextService.getCurrentLoginUser(SecurityUtils.getSubject());
            deptId = userDO.getDeptId();
        }
        DeptDO deptDO=deptService.selectById(deptId);
        String deptName = deptDO.getName();
        Integer deptOrder=deptDO.getOrderNum();
        List<ReportDO> unMSubmitReportList=reportService.getThisWeekReportByDeptAndStatusMSub(deptId,0);
        List<ReportDO> mSubmitReportList=reportService.getThisWeekReportByDeptAndStatusMSub(deptId,1);

        /*获取专题周报统计信息*/
        TopicReportStatisticVO topicReportStatistic=new TopicReportStatisticVO();
        List<TopicReportStatisticVO> topicReportStatisticList=new ArrayList<>();
        int totalCount=unMSubmitReportList.size()+mSubmitReportList.size();
        int unMSubmitCount=unMSubmitReportList.size();
        String unMSubmitUserList="";
        for(int i=0;i<unMSubmitReportList.size();i++ ){
            if(i<unMSubmitReportList.size()-1)
                unMSubmitUserList+=unMSubmitReportList.get(i).getAuthorName()+"，";
            else  unMSubmitUserList+=unMSubmitReportList.get(i).getAuthorName();
        }
        topicReportStatistic.setDeptName(deptName);
        topicReportStatistic.setTotalCount(totalCount);
        topicReportStatistic.setUnMSubmitCount(unMSubmitCount);
        topicReportStatistic.setUnMSubmitUsers(unMSubmitUserList);
        topicReportStatisticList.add(topicReportStatistic);
        model.addAttribute("topicReportStatisticList", topicReportStatisticList);

        /*获取专题周报详细信息*/
        List<TopicReportDetailsVO> topicReportDetailsList=new ArrayList<>();
        for(int i=0;i<mSubmitReportList.size();i++ ){
            TopicReportDetailsVO topicReportDetails=new TopicReportDetailsVO();
            ReportDO report=mSubmitReportList.get(i);
            topicReportDetails.setDeptName(deptName);
            topicReportDetails.setDeptOrder(deptOrder);
            topicReportDetails.setUserOrder(userService.selectById(report.getAuthorId()).getOrderNum());
            topicReportDetails.setAuthorName(report.getAuthorName());
            ReportContentDO reportContent=reportContentService.getByUUID(report.getContentId());
            topicReportDetails.setMonthPlan("");
            topicReportDetails.setSummary(reportContent.getSummary());
            topicReportDetails.setNextPlan(reportContent.getNextPlan());
            topicReportDetails.setProblem(reportContent.getProblem());
            topicReportDetails.setComment(report.getComment());
            topicReportDetailsList.add(topicReportDetails);
        }
        Collections.sort(topicReportDetailsList);//这里直接在后端排序
        model.addAttribute("topicReportDetailsList", topicReportDetailsList);

        Date mon = DateUtils.getThisWeekMondayStart(new Date());
        Date sun = DateUtils.getThisWeekSundayEnd(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String title = sdf.format(mon) + "-" + sdf.format(sun) + "-" + deptName + " 工作汇总";
        model.addAttribute("title", title);
        return "bio/topic/weekInfo";
    }
    //获取本周学生专题周报提交的状况
    @ResponseBody
    @GetMapping("/submitInfo")
    @RequiresPermissions("bio:topic:topic")
    public Result<List<ReportDO>> getSubmitInfo(){
        UserDO userDO =  contextService.getCurrentLoginUser(SecurityUtils.getSubject());
        Long deptId = userDO.getDeptId();
        List<ReportDO> reportList=reportService.getThisWeekReportByDept(deptId);
        return Result.ok(reportList);
    }

    @RequiresPermissions("bio:topic:topic")
    @Log("提交专题周报汇总")
    @PostMapping("/submitTopicReports")
    @ResponseBody
    Result<String> submitTopicReports(@RequestBody List<ReportVO> reportVOList) {
        UserDO userDO =  contextService.getCurrentLoginUser(SecurityUtils.getSubject());
        //根据前端对学生周报的排序，设置相应的排序并保存（排序因子存于user表）
        for(int i=0;i<reportVOList.size();i++){
            ReportVO reportVO=reportVOList.get(i);
            UserDO user=new UserDO();
            user.setId(reportVO.getAuthorId());
            System.out.println(reportVO.getAuthorId());
            user.setOrderNum(reportVO.getOrderNum());
            userService.updatePersonal(user);
        }
        //生成专题周报汇总
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("dept_id", userDO.getDeptId());
        if(summaryService.selectByMap(columnMap).size()==0) {
            SummaryDO summaryDO = new SummaryDO();
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
            summaryDO.setTitle(title);
            summaryService.insert(summaryDO);
        }

        return Result.ok();
    }
}
