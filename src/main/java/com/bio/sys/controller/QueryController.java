package com.bio.sys.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bio/query")
public class QueryController {
    @GetMapping()
    @RequiresPermissions("bio:query:query")
    public  String Query(){
        return "bio/query/query";
    }

    @GetMapping("/userSelect")
    @RequiresPermissions("bio:query:query")
    public  String userSelect(){
        return "bio/query/userSelect";
    }
}
