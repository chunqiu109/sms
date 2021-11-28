package com.danmi.sms.service;

import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.Authentication;
import com.baomidou.mybatisplus.extension.service.IService;
import com.danmi.sms.entity.request.AuthenticationRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chunqiu
 * @since 2021-11-28
 */
public interface IAuthenticationService extends IService<Authentication> {

    PageDTO<Authentication> listAuthenticationPage(AuthenticationRequest authentication);
}
