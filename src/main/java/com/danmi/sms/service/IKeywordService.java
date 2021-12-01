package com.danmi.sms.service;

import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.Keyword;
import com.baomidou.mybatisplus.extension.service.IService;
import com.danmi.sms.entity.User;
import com.danmi.sms.entity.request.KeywordRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chunqiu
 * @since 2021-11-29
 */
public interface IKeywordService extends IService<Keyword> {

    PageDTO<Keyword> listKeywordPage(KeywordRequest keyword, User loginUser);
}
