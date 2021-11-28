package com.danmi.sms.service;

import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.Template;
import com.baomidou.mybatisplus.extension.service.IService;
import com.danmi.sms.entity.request.TemplateRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chunqiu
 * @since 2021-11-28
 */
public interface ITemplateService extends IService<Template> {

    PageDTO<Template> listTemplatePage(TemplateRequest template);
}
