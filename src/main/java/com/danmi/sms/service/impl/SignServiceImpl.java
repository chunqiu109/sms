package com.danmi.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.Sign;
import com.danmi.sms.entity.request.SignRequest;
import com.danmi.sms.mapper.SignMapper;
import com.danmi.sms.service.ISignService;
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
public class SignServiceImpl extends ServiceImpl<SignMapper, Sign> implements ISignService {

    @Autowired
    private SignMapper signMapper;

    @Override
    public PageDTO<Sign> listSignPage(SignRequest request) {
        Integer pageNum = request.getPage();
        Integer pageSize = request.getLimit();
        if (pageSize==null || pageSize.equals(0)) {
            pageSize = 10;
        }
        if (pageNum==null || pageNum.equals(0)) {
            pageNum = 1;
        }
        IPage<Sign> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Sign> wrapper = Wrappers.<Sign>lambdaQuery()
                .eq(StringUtils.isNotBlank(request.getApproveStatus()),Sign::getApproveStatus, request.getApproveStatus())
                .like(StringUtils.isNotBlank(request.getContent()),Sign::getContent, request.getContent());

        IPage<Sign> data = signMapper.selectPage(page, wrapper);
        return new PageDTO<>(data);
    }
}
