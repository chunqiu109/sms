package com.danmi.sms.service;

import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.Menu;
import  com.danmi.sms.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.danmi.sms.entity.request.UserRequest;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chunqiu
 * @since 2021-11-19
 */
public interface IUserService extends IService<User> {
    User login(User user);

    PageDTO<User> listUserPage(Integer roleId, UserRequest param);
}
