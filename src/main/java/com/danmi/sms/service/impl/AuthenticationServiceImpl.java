package com.danmi.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.Authentication;
import com.danmi.sms.entity.request.AuthenticationRequest;
import com.danmi.sms.mapper.AuthenticationMapper;
import com.danmi.sms.service.IAuthenticationService;
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
public class AuthenticationServiceImpl extends ServiceImpl<AuthenticationMapper, Authentication> implements IAuthenticationService {

    @Autowired
    private AuthenticationMapper authenticationMapper;

    @Override
    public PageDTO<Authentication> listAuthenticationPage(AuthenticationRequest request) {
        Integer pageNum = request.getPage();
        Integer pageSize = request.getLimit();
        if (pageSize==null || pageSize.equals(0)) {
            pageSize = 10;
        }
        if (pageNum==null || pageNum.equals(0)) {
            pageNum = 1;
        }
        IPage<Authentication> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Authentication> wrapper = Wrappers.<Authentication>lambdaQuery()
                .eq(StringUtils.isNotBlank(request.getApproveStatus()),Authentication::getApproveStatus, request.getApproveStatus())
                .like(StringUtils.isNotBlank(request.getCompany()),Authentication::getCompany, request.getCompany())
                .like(StringUtils.isNotBlank(request.getLegalPerson()),Authentication::getLegalPerson, request.getLegalPerson())
                .like(StringUtils.isNotBlank(request.getCreditCode()),Authentication::getCreditCode, request.getCreditCode());

        IPage<Authentication> data = authenticationMapper.selectPage(page, wrapper);
        return new PageDTO<>(data);
    }
}
