package com.danmi.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.*;
import com.danmi.sms.entity.request.KeywordRequest;
import com.danmi.sms.mapper.KeywordMapper;
import com.danmi.sms.mapper.SignMapper;
import com.danmi.sms.service.IKeywordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.danmi.sms.service.IRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chunqiu
 * @since 2021-11-29
 */
@Service
public class KeywordServiceImpl extends ServiceImpl<KeywordMapper, Keyword> implements IKeywordService {

    @Autowired
    private KeywordMapper keywordMapper;
    @Autowired
    private IRoleService roleService;

    @Override
    public PageDTO<Keyword> listKeywordPage(KeywordRequest request, User loginUser) {
        Integer pageNum = request.getPage();
        Integer pageSize = request.getLimit();
        if (pageSize==null || pageSize.equals(0)) {
            pageSize = 10;
        }
        if (pageNum==null || pageNum.equals(0)) {
            pageNum = 1;
        }

        Role role = roleService.getById(loginUser.getRoleId());
        Boolean isSuperAdmin = "system_admin".equals(role.getCode());

        IPage<Keyword> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Keyword> wrapper = Wrappers.<Keyword>lambdaQuery()
//                .eq(StringUtils.isNotBlank(request.getApproveStatus()),Sign::getApproveStatus, request.getApproveStatus())
                .like(StringUtils.isNotBlank(request.getKeyword()),Keyword::getKeyword, request.getKeyword());

        IPage<Keyword> data = keywordMapper.selectPage(page, wrapper);

        if (!isSuperAdmin) { // 不是超级管理员，只可以查看自己创建的人员
            List<Keyword> collect = data.getRecords().stream().filter(i -> i.getCa().substring(0, loginUser.getCode().length() + 1).equals(loginUser.getCode())).collect(Collectors.toList());
            data.setRecords(collect);
        }

        return new PageDTO<>(data);
    }
}
