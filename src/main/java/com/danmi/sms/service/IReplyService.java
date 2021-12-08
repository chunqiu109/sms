package com.danmi.sms.service;

import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.Reply;
import com.baomidou.mybatisplus.extension.service.IService;
import com.danmi.sms.entity.User;
import com.danmi.sms.entity.request.ReplyRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chunqiu
 * @since 2021-12-08
 */
public interface IReplyService extends IService<Reply> {

    PageDTO<Reply> listReplyPage(ReplyRequest request, User loginUser);
}
