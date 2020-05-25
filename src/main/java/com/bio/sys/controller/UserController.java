package com.bio.sys.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bio.sys.domain.*;
import com.bio.sys.service.*;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.bio.common.annotation.Log;
import com.bio.common.base.BaseController;
import com.bio.common.domain.Tree;
import com.bio.common.service.ContextService;
import com.bio.common.utils.MD5Utils;
import com.bio.common.utils.Result;
import com.bio.sys.vo.UserVO;

@RequestMapping("/sys/user")
@Controller
public class UserController extends BaseController {
	
    private String prefix = "sys/user";
    
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ContextService contextService;

    @Autowired 
    private DeptService deptService;

    @Autowired
    private UserPlanService userPlanService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportContentService reportContentService;
    
    @RequiresPermissions("sys:user:user")
    @GetMapping("")
    String user(Model model) {
        return prefix + "/user";
    }

    @GetMapping("/list")
    @ResponseBody
    public Result<Page<UserDO>> list(Integer pageNumber, Integer pageSize, UserDO userDTO) {
        // 查询列表数据
        Page<UserDO> page = new Page<>(pageNumber, pageSize);

        Wrapper<UserDO> wrapper = new EntityWrapper<UserDO>(userDTO);

		UserDO userDO =  contextService.getCurrentLoginUser(SecurityUtils.getSubject());

		String inDeptIds = "";

        page = userService.selectPage(page, wrapper);
        int total = userService.selectCount(wrapper);
        page.setTotal(total);
        return Result.ok(page);
    }

    @GetMapping("/topicList")
    @ResponseBody
    public Result<Page<UserDO>> topicList(Integer pageNumber, Integer pageSize, UserDO userDTO) {//专题组长和杜师姐
        // 查询列表数据
        Page<UserDO> page = new Page<>(pageNumber, pageSize);

        Wrapper<UserDO> wrapper = new EntityWrapper<UserDO>(userDTO);

        UserDO userDO =  contextService.getCurrentLoginUser(SecurityUtils.getSubject());

        String inDeptIds = "";
        if(userDO.getroleId().intValue() == 2||userDO.getroleId().intValue() == 4)//专题组长和杜师姐
        {
            inDeptIds = userDO.getDeptId().toString();
            List<DeptDO> deptDOs = deptService.getSubDepts(userDO.getDeptId());
            for(DeptDO deptDO : deptDOs) {
                inDeptIds +="," + deptDO.getId();
            }
            wrapper.in("dept_id", inDeptIds);
        }

        page = userService.selectPage(page, wrapper);
        int total = userService.selectCount(wrapper);
        page.setTotal(total);
        return Result.ok(page);
    }

    @RequiresPermissions("sys:user:add")
    @Log("添加用户")
    @GetMapping("/add")
    String add(Model model) {
        UserDO userDO =  contextService.getCurrentLoginUser(SecurityUtils.getSubject());
        List<RoleDO> roles = roleService.findListByUserId(userDO.getId());
        model.addAttribute("roles", roles);
        return prefix + "/add";
    }

    @RequiresPermissions("sys:user:edit")
    @Log("编辑用户")
    @GetMapping("/edit/{id}")
    String edit(Model model, @PathVariable("id") Long id) {
        UserDO userDO = userService.selectById(id);
        model.addAttribute("user", userDO);
        UserDO leaderDO =  contextService.getCurrentLoginUser(SecurityUtils.getSubject());
        List<RoleDO> roles = roleService.findListByUserId(leaderDO.getId());
        model.addAttribute("roles", roles);
        return prefix + "/edit";
    }



    @RequiresPermissions("sys:user:add")
    @Log("保存用户")
    @PostMapping("/save")
    @ResponseBody
    Result<String> save(UserDO user) {
        user.setPassword(MD5Utils.encrypt(user.getUsername(), user.getPassword()));
        userService.insert(user);
        return Result.ok();
    }

    @RequiresPermissions("sys:user:edit")
    @Log("更新用户")
    @PostMapping("/update")
    @ResponseBody
    Result<String> update(UserDO user) {
        userService.updateById(user);
        return Result.ok();
    }

    @RequiresPermissions("sys:user:edit")
    @Log("更新用户")
    @PostMapping("/updatePeronal")
    @ResponseBody
    Result<String> updatePeronal(UserDO user) {
        userService.updatePersonal(user);
        return Result.ok();
    }


    @RequiresPermissions("sys:user:remove")
    @Log("删除用户")
    @PostMapping("/remove")
    @ResponseBody
    Result<String> remove(Long id) {
        userService.deleteById(id);
        return Result.ok();
    }

    @RequiresPermissions("sys:user:batchRemove")
    @Log("批量删除用户")
    @PostMapping("/batchRemove")
    @ResponseBody
    Result<String> batchRemove(@RequestParam("ids[]") Long[] userIds) {
        userService.deleteBatchIds(Arrays.asList(userIds));
        return Result.ok();
    }

    @PostMapping("/exit")
    @ResponseBody
    boolean exit(@RequestParam Map<String, Object> params) {
        // 存在，不通过，false
        return !userService.exit(params);
    }

    @RequiresPermissions("sys:user:resetPwd")
    @Log("请求更改用户密码")
    @GetMapping("/resetPwd/{id}")
    String resetPwd(@PathVariable("id") Long userId, Model model) {

        UserDO userDO = new UserDO();
        userDO.setId(userId);
        model.addAttribute("user", userDO);
        return prefix + "/reset_pwd";
    }

    @Log("提交更改用户密码")
    @PostMapping("/resetPwd")
    @ResponseBody
    Result<String> resetPwd(UserVO userVO) {
        userService.resetPwd(userVO, getUser());
        return Result.ok();
    }

    @RequiresPermissions("sys:user:resetPwd")
    @Log("admin提交更改用户密码")
    @PostMapping("/adminResetPwd")
    @ResponseBody
    Result<String> adminResetPwd(UserVO userVO) {
        userService.adminResetPwd(userVO);
        return Result.ok();

    }

    @GetMapping("/tree")
    @ResponseBody
    public Tree<DeptDO> tree() {
        Tree<DeptDO> tree = new Tree<DeptDO>();
        tree = userService.getTree();
        return tree;
    }

    @GetMapping("/treeView")
    String treeView() {
        return prefix + "/userTree";
    }

    @GetMapping("/personal")
    String personal(Model model) {
        UserDO userDO = userService.selectById(getUserId());
        model.addAttribute("user", userDO);
        return prefix + "/personal";
    }

    @ResponseBody
    @PostMapping("/uploadImg")
    Result<?> uploadImg(@RequestParam("avatar_file") MultipartFile file, String avatar_data, HttpServletRequest request)
            throws Exception {
        Map<String, Object> result = new HashMap<>();
        result = userService.updatePersonalImg(file, avatar_data, getUserId());
        return Result.ok(result);
    }

    @RequiresPermissions("sys:user:edit")
    @Log("给用户设定目标")
    @GetMapping("/userPlan/{id}")
    String userPlan(Model model, @PathVariable("id") Long id) {
        UserDO userDO = userService.selectById(id);
        model.addAttribute("user", userDO);

        UserPlanDO userPlan=userPlanService.getByAuthorId(id);
        if(userPlan==null) userPlan=new UserPlanDO();
        UserDO leaderDO =  contextService.getCurrentLoginUser(SecurityUtils.getSubject());
        List<RoleDO> roles = roleService.findListByUserId(leaderDO.getId());
        model.addAttribute("userPlan", userPlan);
        return prefix + "/setPlan";
    }

    @RequiresPermissions("sys:user:edit")
    @Log("更新目标")
    @PostMapping("/updateUserPlan")
    @ResponseBody
    Result updateUserPlan( UserPlanDO userPlan) {
        //更新user_plan表
       if(userPlan.getId()==null)
           userPlanService.insert(userPlan);
       else
           userPlanService.updateById(userPlan);
       //同时更新该用户本周周报里的本月目标
       ReportDO reportDO=reportService.getThisWeekReportByAuthorId(userPlan.getAuthorId());
       ReportContentDO reportContentDO=reportContentService.getByUUID(reportDO.getContentId());
       reportContentDO.setMonthPlan(userPlan.getMonthPlan());
       reportContentService.updateById(reportContentDO);
       return Result.ok();
    }

}
