package com.bio.sys.controller;

import com.bio.common.utils.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/bio/topic")
public class TopicController {
    @GetMapping()
    @RequiresPermissions("bio:topic:topic")
    public String Topic(){
        return "bio/topic/topic";
    }

    @ResponseBody
    @GetMapping("/weekList")
    @RequiresPermissions("bio:topic:topic")
    public Result<String> weekList(Integer pageNumber, Integer pageSize){
        return Result.ok();
    }
}
