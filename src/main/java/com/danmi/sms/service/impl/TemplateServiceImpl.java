package com.danmi.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.Template;
import com.danmi.sms.entity.request.TemplateRequest;
import com.danmi.sms.mapper.TemplateMapper;
import com.danmi.sms.service.ITemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chunqiu
 * @since 2021-11-28
 */
@Service
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, Template> implements ITemplateService {

    @Autowired
    private TemplateMapper templateMapper;

    @Override
    public PageDTO<Template> listTemplatePage(TemplateRequest request) {
        Integer pageNum = request.getPage();
        Integer pageSize = request.getLimit();
        if (pageSize==null || pageSize.equals(0)) {
            pageSize = 10;
        }
        if (pageNum==null || pageNum.equals(0)) {
            pageNum = 1;
        }
        IPage<Template> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Template> wrapper = Wrappers.<Template>lambdaQuery()
                .eq(StringUtils.isNotBlank(request.getApproveStatus()),Template::getApproveStatus, request.getApproveStatus())
                .like(StringUtils.isNotBlank(request.getContent()),Template::getContent, request.getContent());

        IPage<Template> data = templateMapper.selectPage(page, wrapper);
        return new PageDTO<>(data);
    }
}
