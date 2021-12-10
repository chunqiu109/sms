package com.danmi.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.danmi.sms.common.constants.MyConstants;
import com.danmi.sms.common.vo.Result;
import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.Role;
import  com.danmi.sms.entity.User;
import com.danmi.sms.entity.request.UserRequest;
import  com.danmi.sms.mapper.UserMapper;
import com.danmi.sms.service.IMenuService;
import com.danmi.sms.service.IRoleService;
import  com.danmi.sms.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.danmi.sms.utils.UserCodeUtil;
import com.danmi.sms.vo.UserVo;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private IRoleService roleService;
    @Resource
    private IMenuService menuService;

    @Value("${role.digit:5}")
    private Integer roleDigit;

    @Override
    public User login(User user) {
        return userMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getPhone, user.getPhone()));
    }

    @Override
    public PageDTO<User> listUserPage(User loginUser, UserRequest param) {
        Integer roleId = loginUser.getRoleId();

        Integer pageNum = param.getPage();
        Integer pageSize = param.getLimit();
        if (pageSize==null || pageSize.equals(0)) {
            pageSize = 10;
        }
        if (pageNum==null || pageNum.equals(0)) {
            pageNum = 1;
        }

        Role role = roleService.getById(roleId);
        Boolean isSuperAdmin = "system_admin".equals(role.getCode());

        IPage<User> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery().like(StringUtils.isNotBlank(param.getPhone()), User::getPhone, param.getPhone());

        IPage<User> data = userMapper.selectPage(page, wrapper);

        if (!isSuperAdmin) { // 不是超级管理员，只可以查看自己创建的人员
            List<User> collect = data.getRecords().stream().filter(i -> i.getCode().substring(0, loginUser.getCode().length()).equals(loginUser.getCode())).collect(Collectors.toList());
            data.setRecords(collect);
        }

        return new PageDTO<>(data);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result register(UserVo user) {

        // 生成用户编码
        String userCode;
        int count = count(Wrappers.<User>lambdaQuery().isNotNull(User::getCode));

        // 说明已经有了企业注册，不需要初始化用户code
        if (count != 0) {
            String code = list(Wrappers.<User>lambdaQuery().select(User::getCode).orderByDesc(User::getCode)).get(0).getCode();
            Integer num = Integer.valueOf(code);
            userCode = String.valueOf(num + 1);
        } else {
            userCode = UserCodeUtil.generateRegisterCode();
        }

        List<Integer> ids = menuService.list().stream().map(menu -> menu.getId()).collect(Collectors.toList());
        String join = Joiner.on(",").join(ids);
        // 先保存角色信息
        Role role = new Role().setCa(userCode)
                .setMenu(join)
                .setCode(MyConstants.COMPANY_ROLE_CODE)
                .setName(MyConstants.COMPANY_ROLE_NAME)
                .setCt(LocalDateTime.now());
        roleService.save(role);

        // 保存
        boolean flag = save(new User().setPassword(user.getPassword())
                .setPhone(user.getPhone())
                .setCode(userCode)
                .setGrade(userCode.length()/roleDigit)
                .setRoleId(role.getId())
                .setCt(LocalDateTime.now()));

        if (flag) {
            return Result.success("注册成功！");
        } else {
            return Result.fail("注册失败！");
        }
    }

    @Override
    public Result<Object> changePassword(UserVo userVo) {
        User user = getById(userVo.getId());
        if (user == null) {
            return Result.fail("没有此用户！");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 参数1是请求密码，参数2是数据库中加密的值
        boolean matches = passwordEncoder.matches(userVo.getOldPassword(), user.getPassword());
        if (!matches) {
            return Result.fail("原密码不正确！");
        }

        String encodePassword = passwordEncoder.encode(userVo.getPassword());
        // 修改
        updateById(new User().setPassword(encodePassword).setId(userVo.getId()));
        return Result.success();
    }
}
