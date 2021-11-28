package com.danmi.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.danmi.sms.dto.PageDTO;
import  com.danmi.sms.entity.Customer;
import com.danmi.sms.entity.request.CustomerRequest;
import  com.danmi.sms.mapper.CustomerMapper;
import  com.danmi.sms.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chunqiu
 * @since 2021-11-19
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public PageDTO<Customer> listCustomerPage(CustomerRequest request) {
        Integer pageNum = request.getPage();
        Integer pageSize = request.getLimit();
        if (pageSize==null || pageSize.equals(0)) {
            pageSize = 10;
        }
        if (pageNum==null || pageNum.equals(0)) {
            pageNum = 1;
        }
        IPage<Customer> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Customer> wrapper = Wrappers.<Customer>lambdaQuery()
                .like(StringUtils.isNotBlank(request.getName()), Customer::getName, request.getName())
                .like(StringUtils.isNotBlank(request.getPhone()), Customer::getPhone, request.getPhone())
                .ge(request.getStartDate()!=null, Customer::getCt,request.getStartDate())
                .le(request.getEndDate()!=null, Customer::getCt,request.getEndDate())
                .orderByDesc(Customer::getId);


        IPage<Customer> data = customerMapper.selectPage(page, wrapper);
        return new PageDTO<>(data);
    }

    @Override
    public List<Customer> export(CustomerRequest request) {
        return customerMapper.selectList( Wrappers.<Customer>lambdaQuery()
                .like(StringUtils.isNotBlank(request.getName()), Customer::getName, request.getName())
                .like(StringUtils.isNotBlank(request.getPhone()), Customer::getPhone, request.getPhone())
                .ge(request.getStartDate()!=null, Customer::getCt,request.getStartDate())
                .le(request.getEndDate()!=null, Customer::getCt,request.getEndDate()));
    }
}
