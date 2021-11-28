package com.danmi.sms.controller;


import com.danmi.sms.common.vo.Result;
import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.Menu;
import com.danmi.sms.entity.Role;
import com.danmi.sms.entity.request.MenuRequest;
import com.danmi.sms.service.IMenuService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

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

    @GetMapping("/list")
    @ResponseBody
    @ApiOperation(value = "获取菜单列表", notes = "获取菜单列表")
    public Result<Object> getMenuList() {
        return Result.success( menuService.findTree(null));
    }

    @PostMapping("")
    @ResponseBody
    @ApiOperation(value = "添加菜单", notes = "不传递父菜单ID，默认为父菜单")
    public Result<Object> addMenu(@RequestBody Menu menu) {
        // 判断必须参数
        if (!StringUtils.hasLength(menu.getName()) || !StringUtils.hasLength(menu.getCode())) {
            return Result.fail("必传参数不能为空！");
        }

        if (menu.getParentId() == null) {
            menu.setParentId(0);
        }
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
