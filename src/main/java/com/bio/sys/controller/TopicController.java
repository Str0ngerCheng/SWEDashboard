package com.bio.sys.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.bio.common.service.ContextService;
import com.bio.common.utils.PageUtil;
import com.bio.common.utils.Result;
import com.bio.sys.domain.ReportContentDO;
import com.bio.sys.domain.ReportDO;
import com.bio.sys.domain.UserDO;
import com.bio.sys.service.DeptService;
import com.bio.sys.service.ReportContentService;
import com.bio.sys.service.ReportService;
import com.bio.sys.service.UserService;
import com.bio.sys.vo.ReportVO;
import com.bio.sys.vo.TopicReportDetailsVO;
import com.bio.sys.vo.TopicReportStatisticVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/bio/topic")
public class TopicController {
    @Autowired
    private ReportService reportService;
    @Autowired
    private ReportContentService reportContentService;
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

    @GetMapping("/weekInfo")
    @RequiresPermissions("bio:topic:topic")
    public String getWeekInfo(Model model){
        UserDO userDO =  contextService.getCurrentLoginUser(SecurityUtils.getSubject());
        Long deptId = userDO.getDeptId();
        String deptName=deptService.selectById(deptId).getName();
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
            topicReportDetails.setAuthorName(report.getAuthorName());
            ReportContentDO reportContent=reportContentService.getByUUID(report.getContentId());
            topicReportDetails.setSummary(reportContent.getSummary());
            topicReportDetails.setNextPlan(reportContent.getNextPlan());
            topicReportDetailsList.add(topicReportDetails);
        }
        model.addAttribute("topicReportDetailsList", topicReportDetailsList);

        return "bio/topic/weekInfo";
    }
}
