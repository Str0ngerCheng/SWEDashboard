package com.bio.sys.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.bio.common.service.ContextService;
import com.bio.common.utils.PageUtil;
import com.bio.common.utils.Result;
import com.bio.sys.domain.ReportDO;
import com.bio.sys.domain.UserDO;
import com.bio.sys.service.ReportService;
import com.bio.sys.service.UserService;
import com.bio.sys.vo.ReportVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/bio/topic")
public class TopicController {
    @Autowired
    private ReportService reportService;
    @Autowired
    private UserService userService;
    @Autowired
    private ContextService contextService;
    @GetMapping()
    @RequiresPermissions("bio:topic:topic")
    public String Topic(){
        return "bio/topic/topic";
    }

    @ResponseBody
    @GetMapping("/weekList")
    @RequiresPermissions("bio:topic:topic")
    public Result<Page<ReportVO>> weekList(Integer pageNumber, Integer pageSize,Integer statusLSub){
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
            Page<ReportVO> page = PageUtil.getPage(pageNumber,pageSize,reportVOList);
            return Result.ok(page);
        }
        return Result.fail();
    }
}
