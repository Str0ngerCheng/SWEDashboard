package com.bio.sys.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.bio.common.service.ContextService;
import com.bio.common.utils.Result;
import com.bio.sys.domain.ReportContentDO;
import com.bio.sys.domain.ReportDO;
import com.bio.sys.domain.UserDO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/bio/topic")
public class TopicController {
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
    public Result<String> weekList(Integer pageNumber, Integer pageSize,ReportContentDO reportContentDO){
       /* UserDO userDO =  contextService.getCurrentLoginUser(SecurityUtils.getSubject());

        if(userDO.getroleId().intValue() == 1) { // 超级管理员，默认所有的报告
            //TODO: 不做处理
        }

        if(userDO.getroleId().intValue() == 2) { // 专题组长，只显示负责的学生 的报告
            reportContentDO.setParentDeptId(userDO.getDeptId());
        }

        if(userDO.getroleId().intValue() == 3) { // 学生，只显示自己的报告
            reportDTO.setAuthorId(userDO.getId());
        }

        // 查询列表数据
        Page<ReportDO> page = new Page<>(pageNumber, pageSize);

        Wrapper<ReportDO> wrapper = new EntityWrapper<ReportDO>(reportDTO);
        wrapper.orderBy("r_create_date", false);
        page = reportService.selectPage(page, wrapper);
        int total = reportService.selectCount(wrapper);
        page.setTotal(total);*/
        return Result.ok();
    }

  /*  @ResponseBody
    @GetMapping("/btnExcelSubject")
    @RequiresPermissions("bio:topic:topic")
    public List<ReportContentDO> btnExcelSubject(){

    }*/
}
