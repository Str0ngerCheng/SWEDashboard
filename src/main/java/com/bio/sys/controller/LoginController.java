package com.bio.sys.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bio.common.service.ContextService;
import com.bio.sys.domain.UserDO;
import com.bio.sys.domain.UserPlanDO;
import com.bio.sys.service.RoleService;
import com.bio.sys.service.UserPlanService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bio.common.annotation.Log;
import com.bio.common.base.BaseController;
import com.bio.common.domain.Tree;
import com.bio.common.type.EnumErrorCode;
import com.bio.common.utils.MD5Utils;
import com.bio.common.utils.Result;
import com.bio.common.utils.ShiroUtils;
import com.bio.oss.domain.FileDO;
import com.bio.oss.service.FileService;
import com.bio.sys.domain.MenuDO;
import com.bio.sys.service.MenuService;

/**
 * <pre>
 * </pre>
 * <small> 2018年3月23日 | Aron</small>
 */
@Controller
public class LoginController extends BaseController {

    @Autowired
    MenuService menuService;
    @Autowired
    FileService fileService;
    @Autowired
    private UserPlanService userPlanService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ContextService contextService;

    @GetMapping({ "/", "" })
    String welcome(Model model) {
        return "redirect:/login";
    }

    @Log("请求访问主页")
    @GetMapping({ "/index" })
    String index(Model model) {
        List<Tree<MenuDO>> menus = menuService.listMenuTree(getUserId());
        model.addAttribute("menus", menus);
        model.addAttribute("name", getUser().getName());
        model.addAttribute("username", getUser().getUsername());
        FileDO fileDO = fileService.selectById(getUser().getPicId());
        model.addAttribute("picUrl", fileDO == null ? "/img/photo_s.jpg" : fileDO.getUrl());
        return "index_v1";
    }

    @GetMapping("/login")
    String login() {
        return "login";
    }

    @Log("登录")
    @PostMapping("/login")
    @ResponseBody
    Result<String> ajaxLogin(String username, String password) {
        password = MD5Utils.encrypt(username, password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            return Result.ok();
        } catch (AuthenticationException e) {
            return Result.build(EnumErrorCode.userLoginFail.getCode(), EnumErrorCode.userLoginFail.getMsg());
        }
    }

    @GetMapping("/logout")
    String logout() {
        ShiroUtils.logout();
        return "redirect:/login";
    }

    @GetMapping("/main")
    String main(Model model) {
        UserDO userDO =  contextService.getCurrentLoginUser(SecurityUtils.getSubject());
        UserPlanDO userPlan=userPlanService.getByAuthorId(userDO.getId());

        if(userPlan==null) userPlan=new UserPlanDO();
        model.addAttribute("username", userDO.getName());
        model.addAttribute("userPlan", userPlan);

        Long roleId = roleService.findRoleIdByUserId(userDO.getId());
        if(roleId==5||roleId==1)//陈老师和管理员登录首页不显示目标
            model.addAttribute("haveUserPlan", false);
        else model.addAttribute("haveUserPlan", true);
        return "main";
    }

    @GetMapping("/about")
    String about() {
        return "about";
    }

    @GetMapping("/403")
    String error403() {
        return "403";
    }

}
