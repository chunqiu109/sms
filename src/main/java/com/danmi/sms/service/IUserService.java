package com.danmi.sms.service;

import com.danmi.sms.common.vo.Result;
import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.AuthList;
import  com.danmi.sms.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.danmi.sms.entity.request.UserRequest;
import com.danmi.sms.vo.UserVo;


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

    PageDTO<User> listUserPage(User loginUser, UserRequest param);

    Result register(UserVo user);

    Result<Object> changePassword(UserVo user);

    AuthList getAuthByMenuId(Integer menuId, User user);
}
