package com.danmi.sms.controller;


import com.danmi.sms.common.vo.Result;
import com.danmi.sms.entity.Menu;
import com.danmi.sms.entity.Role;
import com.danmi.sms.entity.User;
import com.danmi.sms.service.IMenuService;
import com.danmi.sms.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 菜单
 *
 * @author chunqiu
 * @since 2021-11-19
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IRoleService roleService;

    @GetMapping("/list")
    @ResponseBody
    @ApiOperation(value = "获取菜单列表", notes = "获取菜单列表")
    public Result<Object> getMenuList(HttpServletRequest request) {
        Object userInfo = request.getSession().getAttribute("userInfo");

        if (!(userInfo instanceof User)) {
            return Result.success("您尚未登录！");
        }
        User loginUser = (User) userInfo;

        List<Integer> list;
        Role role = roleService.getById(loginUser.getRoleId());

        if ("system_admin".equals(role.getCode())) {
            list = null;
        } else {
            String[] split = role.getMenu().split(",");
            list = new ArrayList<>(Arrays.asList(split)).stream().map(i -> Integer.valueOf(i)).collect(Collectors.toList());
        }

        int option = 0;
        return Result.success( menuService.findTree(list, option));
    }

    @PostMapping("")
    @ResponseBody
    @ApiOperation(value = "添加菜单", notes = "不传递父菜单ID，默认为父菜单")
    public Result<Object> addMenu(@RequestBody Menu menu, HttpServletRequest request) {

        Object userInfo = request.getSession().getAttribute("userInfo");

        if (!(userInfo instanceof User)) {
            return Result.success("您尚未登录！");
        }
        User loginUser = (User) userInfo;
        // 判断必须参数
        if (!StringUtils.hasLength(menu.getName()) || !StringUtils.hasLength(menu.getCode())) {
            return Result.fail("必传参数不能为空！");
        }

        if (menu.getParentId() == null) {
            menu.setParentId(0);
        }
        menu.setCa(loginUser.getCode());
        menu.setCt(LocalDateTime.now());
        boolean flag = menuService.save(menu);
        if (flag) {
            return Result.fail("添加成功！");
        } else {
            return Result.fail("添加失败！");
        }

    }

    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "根据id获取菜单", notes = "根据id获取菜单")
    public Result<Object> getById(@PathVariable(value = "id") Integer id) {
        Menu menu = menuService.getById(id);
        return Result.success(menu);
    }

    @DeleteMapping("/{ids}")
    @ResponseBody
    @ApiOperation(value = "根据id批量删除菜单", notes = "根据id批量删除菜单")
    public Result<Object> deleteMenuByIds(@PathVariable("ids") String ids) {
        List<String> cids = Arrays.asList(ids.split(","));
        menuService.removeByIds(cids);
        return Result.success("删除成功！");
    }

    @PutMapping("")
    @ResponseBody
    @ApiOperation(value = "根据id更新菜单", notes = "根据id更新菜单")
    public Result<Object> updateMenuById(@RequestBody Menu menu) {
        if (ObjectUtils.isEmpty(menu.getId())) {
            return Result.fail("必传参数不能为空！");
        }
        boolean flag = menuService.updateById(menu);
        if (flag) {
            return Result.fail("修改成功！");
        } else {
            return Result.fail("修改失败！");
        }
    }

}
