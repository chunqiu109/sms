package com.danmi.sms.service;

import com.danmi.sms.dto.PageDTO;
import  com.danmi.sms.entity.Customer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.danmi.sms.entity.request.CustomerRequest;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chunqiu
 * @since 2021-11-19
 */
public interface ICustomerService extends IService<Customer> {
    PageDTO<Customer> listCustomerPage(CustomerRequest request);

    List<Customer> export(CustomerRequest param);
}
