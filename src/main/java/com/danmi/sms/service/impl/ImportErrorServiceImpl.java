package com.danmi.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.ImportError;
import com.danmi.sms.entity.request.ImportErrorRequest;
import com.danmi.sms.mapper.ImportErrorMapper;
import com.danmi.sms.service.IImportErrorService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chunqiu
 * @since 2021-08-31
 */
@Service
public class ImportErrorServiceImpl extends ServiceImpl<ImportErrorMapper, ImportError> implements IImportErrorService {
    @Autowired
    private ImportErrorMapper importErrorMapper;

    @Override
    public PageDTO<ImportError> listImportErrorPage(ImportErrorRequest request) {
        Integer pageNum = request.getPage();
        Integer pageSize = request.getLimit();
        if (pageSize==null || pageSize.equals(0)) {
            pageSize = 10;
        }
        if (pageNum==null || pageNum.equals(0)) {
            pageNum = 1;
        }
        IPage<ImportError> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<ImportError> wrapper = Wrappers.<ImportError>lambdaQuery()
                .like(StringUtils.isNotBlank(request.getCusName()), ImportError::getCusName, request.getCusName())
                .like(StringUtils.isNotBlank(request.getPhone()), ImportError::getPhone, request.getPhone())
                .ge(request.getStartDate()!=null, ImportError::getCt,request.getStartDate())
                .le(request.getEndDate()!=null, ImportError::getCt,request.getEndDate())
                .orderByDesc(ImportError::getId);



        IPage<ImportError> data = importErrorMapper.selectPage(page, wrapper);
        return new PageDTO<>(data);
    }
}
