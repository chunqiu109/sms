package com.danmi.sms.service.impl;

import com.danmi.sms.entity.SendStatus;
import com.danmi.sms.mapper.SendStatusMapper;
import com.danmi.sms.service.ISendStatusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chunqiu
 * @since 2021-12-08
 */
@Service
public class SendStatusServiceImpl extends ServiceImpl<SendStatusMapper, SendStatus> implements ISendStatusService {

}
