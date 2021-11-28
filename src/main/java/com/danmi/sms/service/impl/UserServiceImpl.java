package com.danmi.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.Role;
import  com.danmi.sms.entity.User;
import com.danmi.sms.entity.request.UserRequest;
import  com.danmi.sms.mapper.UserMapper;
import com.danmi.sms.service.IRoleService;
import  com.danmi.sms.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chunqiu
 * @since 2021-11-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private IRoleService roleService;

    @Override
    public User login(User user) {
        return userMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getPhone, user.getPhone()));
    }

    @Override
    public PageDTO<User> listUserPage(Integer roleId, UserRequest param) {
        Integer pageNum = param.getPage();
        Integer pageSize = param.getLimit();
        if (pageSize==null || pageSize.equals(0)) {
            pageSize = 10;
        }
        if (pageNum==null || pageNum.equals(0)) {
            pageNum = 1;
        }

        Role role = roleService.getById(roleId);
//        Boolean isSuperAdmin = role.getCode().equals("super_admin");
        Boolean isSuperAdmin = "super_admin".equals(role.getCode());

        IPage<User> page = new Page<>(pageNum, pageSize);


        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery().like(StringUtils.isNotBlank(param.getPhone()), User::getPhone, param.getPhone())
                .notIn(!isSuperAdmin, User::getRoleId, role.getId());


        IPage<User> data = userMapper.selectPage(page, wrapper);
        return new PageDTO<>(data);
    }
}
