package com.danmi.sms.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.danmi.sms.vo.UserVo;
import com.danmi.sms.common.vo.Result;
import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.Menu;
import com.danmi.sms.entity.User;
import com.danmi.sms.entity.request.UserRequest;
import com.danmi.sms.service.IRoleService;
import com.danmi.sms.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 用户
 */
@RestController
@RequestMapping("/user")
@Api("用户")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;


//    @todo 权限问题
    @GetMapping("/list")
    @ResponseBody
    @ApiOperation(value = "用户列表", notes = "用户列表")
    public Result list(UserRequest param, HttpServletRequest request) {

        Object userInfo = request.getSession().getAttribute("userInfo");
        User loginUser = new User();
        if (userInfo instanceof User) {
            loginUser = (User) userInfo;
            PageDTO<User> users  = userService.listUserPage(loginUser.getRoleId(), param);
            users.getRecords().stream().map(i -> i.setPassword("")).collect(Collectors.toList());
            return Result.success(users);
        }
        return Result.fail("您尚未登录！");
    }

    @PostMapping("/login")
    @ResponseBody
    @ApiOperation(value = "用户登录", notes = "用户登录")
    public Result login(@RequestBody User param, HttpSession session) {
        if (StringUtils.isEmpty(param.getPhone()) || StringUtils.isEmpty(param.getPassword())) {
            return Result.fail("手机号或者密码不能为空！");
        }

        User user = userService.login(param);
        if (user != null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            // 参数1是请求密码，参数2是数据库中加密的值
            boolean matches = passwordEncoder.matches(param.getPassword(), user.getPassword());
            if (matches) {
                // 登录成功
                user.setPassword(null);
                session.setAttribute("userInfo", user);
                return Result.success();
            }
        }
        // 登录失败
        return Result.fail("用户名或密码错误！");
    }

    @PostMapping("/logout")
    @ResponseBody
    @ApiOperation(value = "用户登出", notes = "用户登出")
    public Result logout(HttpServletRequest request, HttpSession session) {

        session.removeAttribute("userInfo");
        return Result.success("退出成功！");
    }

    @PostMapping("/register")
    @ResponseBody
    @ApiOperation(value = "用户注册", notes = "用户注册")
    @ApiImplicitParams({@ApiImplicitParam(name = "phone", value = "手机号", dataTypeClass = String.class),
            @ApiImplicitParam(name = "password", dataTypeClass = String.class),
            @ApiImplicitParam(name = "secPassword", dataTypeClass = String.class)})
    public Result register(@RequestBody UserVo user) {
        if (StringUtils.isEmpty(user.getPhone())) {
            return Result.fail("手机号不能为空！");
        }

        if (!checkPhone(user.getPhone())) {
            return Result.fail("手机号格式有误！");
        }

        if (StringUtils.isEmpty(user.getPassword())) {
            return Result.fail("密码不能为空！");
        }
        if (StringUtils.isEmpty(user.getSecPassword())) {
            return Result.fail("确认密码不能为空！");
        }
        if (!user.getSecPassword().equals(user.getPassword())) {
            return Result.fail("两次密码输入不一致！");
        }


        User one = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getPhone, user.getPhone()));
        if (one != null) {
            return Result.fail("该手机号已被注册！");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodePassword);

        // 保存
        boolean flag = userService.save(new User().setPassword(user.getPassword())
                .setPhone(user.getPhone())
                .setRoleId(2)
                .setCt(LocalDateTime.now()));

        if (flag) {
            return Result.success("注册成功！");
        } else {
            return Result.fail("注册失败！");
        }
    }

    @PostMapping("")
    @ResponseBody
    @ApiOperation(value = "用户添加", notes = "用户添加")
    public Result<Object> addUser(@RequestBody User user) {
        // 判断必须参数
        if (!org.springframework.util.StringUtils.hasLength(user.getPhone()) || !org.springframework.util.StringUtils.hasLength(user.getPassword())) {
            return Result.fail("必传参数不能为空！");
        }

        User one = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getPhone, user.getPhone()));
        if (one != null) {
            return Result.fail("该手机号已被注册！");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodePassword).setCt(LocalDateTime.now()).setRoleId(2);


        boolean flag = userService.save(user);
        if (flag) {
            return Result.success("添加成功！");
        } else {
            return Result.fail("添加失败！");
        }

    }

    @DeleteMapping("/{ids}")
    @ResponseBody
    @ApiOperation(value = "根据id批量删除用户", notes = "根据id批量删除用户")
    @ApiImplicitParams({@ApiImplicitParam(name = "ids", value = "删除的用户id，多个id逗号分割", dataTypeClass = String.class)})
    public Result<Object> deleteUserByIds(@PathVariable("ids") String ids) {
        List<String> cids = Arrays.asList(ids.split(","));
        userService.removeByIds(cids);
        return Result.success("删除成功！");
    }

    @PutMapping("/getMenuList")
    @ResponseBody
    @ApiOperation(value = "获取当前用户的菜单列表", notes = "获取当前用户的菜单列表")
    public Result<Object> getMenuList(HttpServletRequest request) {
        Object userInfo = request.getSession().getAttribute("userInfo");
        User loginUser = new User();
        if (userInfo instanceof User) {
            loginUser = (User) userInfo;
            List<Menu> menus  = roleService.getMenuList(loginUser.getRoleId());
            return Result.success(menus);
        }
        return Result.fail("获取权限菜单失败：尚未登录！");
    }

    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "根据id获取用户", notes = "根据id获取用户")
    public Result<Object> getById(@PathVariable(value = "id") Integer id) {
        User user = userService.getById(id);
        return Result.success(user);
    }

    public Boolean checkPhone(String phone) {
        Pattern p = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$");
        Matcher m = p.matcher(phone);
        return m.matches();
    }
}
