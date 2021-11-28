package com.danmi.sms.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.ImportError;
import com.danmi.sms.entity.request.ImportErrorRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chunqiu
 * @since 2021-08-31
 */
public interface IImportErrorService extends IService<ImportError> {

    PageDTO<ImportError> listImportErrorPage(ImportErrorRequest param);
}
