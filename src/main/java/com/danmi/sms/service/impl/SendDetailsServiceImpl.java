package com.danmi.sms.service.impl;

import com.danmi.sms.entity.SendDetails;
import com.danmi.sms.entity.request.SendDetailRequest;
import com.danmi.sms.mapper.SendDetailsMapper;
import com.danmi.sms.service.ISendDetailsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.danmi.sms.vo.SendDetailsVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chunqiu
 * @since 2021-11-29
 */
@Service
public class SendDetailsServiceImpl extends ServiceImpl<SendDetailsMapper, SendDetails> implements ISendDetailsService {

}
