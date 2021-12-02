package com.danmi.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.danmi.sms.entity.SendDetails;
import com.danmi.sms.entity.User;
import com.danmi.sms.entity.request.SendDetailRequest;
import com.danmi.sms.mapper.SendDetailsMapper;
import com.danmi.sms.service.ISendDetailsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.danmi.sms.utils.UserUtils;
import com.danmi.sms.vo.SendDetailsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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
public class SendDetailsServiceImpl extends ServiceImpl<SendDetailsMapper, SendDetails> implements ISendDetailsService {
    @Autowired
    private UserUtils userUtils;

    @Override
    public List<SendDetails> sendDetails(SendDetailRequest request) {
        User user = userUtils.getUser();
        LocalDate endTime = request.getEndTime();

        if (!Objects.isNull(request.getEndTime())) {
            request.setEndTime(endTime.plusDays(1L));
        }
        LambdaQueryWrapper<SendDetails> wrapper = Wrappers.<SendDetails>lambdaQuery().gt(!Objects.isNull(request.getStartTime()), SendDetails::getCt, request.getStartTime())
                .le(!Objects.isNull(endTime), SendDetails::getCt, request.getEndTime())
//                .eq(!Objects.isNull(request.getType()), SendDetails::getType, request.getEndTime())
                .eq(!Objects.isNull(request.getStatus()), SendDetails::getStatus, request.getStatus());
        List<SendDetails> list = list(wrapper);

        // 过滤当前登录人自己创建的
        if (!userUtils.isSystemAdmin()) {
            list = list.stream().filter(i -> i.getCa().substring(0, user.getCode().length()+1).equals(user.getCode())).collect(Collectors.toList());
        }

        return list;
    }

    @Override
    public List<SendDetailsVO> replyRecord(SendDetailRequest request) {
        return null;
    }
}
