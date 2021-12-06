package com.danmi.sms.service;

import com.danmi.sms.common.vo.Result;
import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.SendLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.danmi.sms.entity.User;
import com.danmi.sms.entity.request.SendDetailRequest;
import com.danmi.sms.entity.request.SendLogRequest;
import com.danmi.sms.entity.request.SmsRequest;
import com.danmi.sms.vo.SendDetailsVO;
import com.danmi.sms.vo.SendLogFailVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chunqiu
 * @since 2021-11-30
 */
public interface ISendLogService extends IService<SendLog> {

    PageDTO<SendLog> general(SendLogRequest request);

    SendLogFailVO statusAnalysis(SendDetailRequest request, User user);

    List<SendDetailsVO> dataStatistics(SendDetailRequest request);

    Result send(User loginUser, SmsRequest request);

    Result sendAndProcessService(String sendTime, SmsRequest request, User loginUser, List<String> phones, Map<String, Integer> maps, Integer id);
}
