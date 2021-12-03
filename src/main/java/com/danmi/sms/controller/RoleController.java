package com.danmi.sms.controller;


import com.danmi.sms.common.vo.Result;
import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.Menu;
import com.danmi.sms.entity.Role;
import com.danmi.sms.entity.User;
import com.danmi.sms.entity.request.RoleRequest;
import com.danmi.sms.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 角色
 * @author chunqiu
 * @since 2021-11-19
 */
@RestController
@RequestMapping("/role")
@Api("角色管理")
public class RoleController {
    
    @Autowired
    private IRoleService roleService;

    @GetMapping("/list")
    @ResponseBody
    @ApiOperation(value = "获取角色列表（分页）", notes = "获取角色列表（分页）")
    public Result<Object> getRoleList(RoleRequest role, HttpServletRequest request) {

        Object userInfo = request.getSession().getAttribute("userInfo");

        if (!(userInfo instanceof User)) {
            return Result.success("您尚未登录！");
        }
        User loginUser = (User) userInfo;
        PageDTO<Role> rolePageDTO = roleService.listRolePage(role, loginUser);
        return Result.success(rolePageDTO.getRecords(), rolePageDTO.getTotal());
    }

    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "根据id获取角色", notes = "根据id获取角色")
    public Result<Object> getById(@PathVariable(value = "id") Integer id) {
        Role role = roleService.getById(id);
        return Result.success(role);
    }

    @PostMapping("")
    @ResponseBody
    @ApiOperation(value = "添加角色", notes = "添加角色")
    public Result<Object> addRole(@RequestBody Role role, HttpServletRequest request) {
        Object userInfo = request.getSession().getAttribute("userInfo");

        if (!(userInfo instanceof User)) {
            return Result.success("您尚未登录！");
        }
        User loginUser = (User) userInfo;

        // 判断必须参数
        if (!StringUtils.hasLength(role.getName()) || !StringUtils.hasLength(role.getCode())) {
            return Result.fail("必传参数不能为空！");
        }

        role.setCa(loginUser.getCode());
        role.setCt(LocalDateTime.now());
        boolean flag = roleService.save(role);
        if (flag) {
            return Result.success("添加成功！");
        } else {
            return Result.fail("添加失败！");
        }

    }

    @DeleteMapping("/{ids}")
    @ResponseBody
    @ApiOperation(value = "根据id批量删除角色", notes = "根据id批量删除角色")
    @ApiImplicitParams({@ApiImplicitParam(name = "ids", value = "删除的角色id，多个id逗号分割", dataTypeClass = String.class)})
    public Result<Object> deleteRoleByIds(@PathVariable("ids") String ids) {
        List<String> cids = Arrays.asList(ids.split(","));
        roleService.removeByIds(cids);
        return Result.success("删除成功！");
    }

    @PutMapping("")
    @ResponseBody
    @ApiOperation(value = "根据id更新角色", notes = "根据id更新角色")
    public Result<Object> updateRoleById(@RequestBody Role role) {
        if (ObjectUtils.isEmpty(role.getId())) {
            return Result.fail("必传参数不能为空！");
        }
        boolean flag = roleService.updateById(role);
        if (flag) {
            return Result.success("修改成功！");
        } else {
            return Result.fail("修改失败！");
        }
    }

    @PutMapping("/getMenuList")
    @ResponseBody
    @ApiOperation(value = "不测试", notes = "不测试")
    public Result<Object> getMenuList() {
        List<Menu> menus  = roleService.getMenuList(1);
        return Result.success(menus);
    }

    /**
     * 给角色添加菜单权限
     * @param role
     * @return
     */
    @PutMapping("/authMenu")
    @ResponseBody
    @ApiOperation(value = "给角色授权菜单权限", notes = "给角色授权菜单权限")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "角色id", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "menu", value = "菜单id, 多个英文都好分割", dataTypeClass = String.class)})
    public Result<Object> authMenu(@RequestBody Role role) {
        if (ObjectUtils.isEmpty(role.getId()) || !StringUtils.hasLength(role.getMenu())) {
            return Result.fail("必传参数不能为空！");
        }
        boolean flag = roleService.updateById(new Role().setId(role.getId()).setMenu(role.getMenu()));
        if (flag) {
            return Result.success("授权成功！");
        } else {
            return Result.fail("授权失败！");
        }
    }
}
