package com.danmi.sms.service;

import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.Sign;
import com.baomidou.mybatisplus.extension.service.IService;
import com.danmi.sms.entity.User;
import com.danmi.sms.entity.request.SignRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chunqiu
 * @since 2021-11-28
 */
public interface ISignService extends IService<Sign> {

    PageDTO<Sign> listSignPage(SignRequest sign, User loginUser);
}
