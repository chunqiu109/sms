package com.danmi.sms.schedule;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.danmi.sms.entity.SendDetails;
import com.danmi.sms.entity.SendLog;
import com.danmi.sms.entity.User;
import com.danmi.sms.entity.request.SmsRequest;
import com.danmi.sms.enums.SmsSendStatusEnum;
import com.danmi.sms.service.ISendDetailsService;
import com.danmi.sms.service.ISendLogService;
import com.danmi.sms.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TransmissionSms {

    @Autowired
    private ISendLogService sendLogService;
    @Autowired
    private ISendDetailsService sendDetailsService;

    /**
     * 定时发送短信 每5分钟一次
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void sendSms() {
        // 查询定时任务
        String string = DateUtils.localDateTimeToString(LocalDateTime.now());

        List<SendLog> list = sendLogService.list(Wrappers.<SendLog>lambdaQuery().eq(SendLog::getType, 2).eq(SendLog::getStatus, SmsSendStatusEnum.TIME_SEND));

        // 查询当前时间之前的定时任务
        list = list.stream().filter(i -> DateUtils.stringToTimestamp(i.getSendTime())<=DateUtils.localDateTimeToTimestamp(LocalDateTime.now())).collect(Collectors.toList());

        if (ObjectUtils.isNotEmpty(list) && !list.isEmpty()) {
            list.forEach(i -> {
                List<SendDetails> sendDetailsList = sendDetailsService.list(Wrappers.<SendDetails>lambdaQuery().eq(SendDetails::getBatch, i.getBatch()));
                List<String> phones = sendDetailsList.stream().map(m -> m.getPhone()).collect(Collectors.toList());
                Map<String, Integer> maps = sendDetailsList.stream().collect(Collectors.toMap(SendDetails::getPhone, SendDetails::getId, (key1, key2) -> key2));
                SmsRequest smsRequest = new SmsRequest().setBatchNO(i.getBatch()).setContent(i.getContent()).setType(i.getType());
                User user = new User().setCode("system");
                sendLogService.sendAndProcessService(i.getSendTime(), smsRequest, user, phones, maps, i.getId());
            });
        }
    }
}
