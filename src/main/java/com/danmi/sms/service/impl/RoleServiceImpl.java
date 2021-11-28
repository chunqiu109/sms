package com.danmi.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.Menu;
import  com.danmi.sms.entity.Role;
import com.danmi.sms.entity.request.RoleRequest;
import  com.danmi.sms.mapper.RoleMapper;
import com.danmi.sms.service.IMenuService;
import  com.danmi.sms.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chunqiu
 * @since 2021-11-19
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private IMenuService menuService;

    @Override
    public PageDTO<Role> listRolePage(RoleRequest request) {
        Integer pageNum = request.getPage();
        Integer pageSize = request.getLimit();
        if (pageSize==null || pageSize.equals(0)) {
            pageSize = 10;
        }
        if (pageNum==null || pageNum.equals(0)) {
            pageNum = 1;
        }
        IPage<Role> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Role> wrapper = Wrappers.<Role>lambdaQuery()
                .like(StringUtils.isNotBlank(request.getCode()),Role::getCode, request.getCode())
                .like(StringUtils.isNotBlank(request.getName()),Role::getName, request.getName());

        IPage<Role> data = roleMapper.selectPage(page, wrapper);
        return new PageDTO<>(data);
    }

    @Override
    public List<Menu> getMenuList(Integer roleId) {
        Role role = this.getById(roleId);
        String[] menuIds = role.getMenu().split(",");

        List<Integer> list = Arrays.stream(menuIds).filter(i -> StringUtils.isNotEmpty(i)).map(i -> Integer.valueOf(i)).collect(Collectors.toList());

        return menuService.findTree(list);
    }
}
