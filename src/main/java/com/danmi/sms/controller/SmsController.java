package com.danmi.sms.controller;

import com.danmi.sms.common.vo.Result;
import com.danmi.sms.entity.*;
import com.danmi.sms.entity.request.SmsRequest;
import com.danmi.sms.entity.response.SmsResponse;
import com.danmi.sms.service.ISendDetailsService;
import com.danmi.sms.service.ISendLogService;
import com.danmi.sms.utils.PhoneUtil;
import com.danmi.sms.utils.SMSUtils;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

/**
 * 短信服务
 *
 * @author chunqiu
 * @since 2021-11-28
 */
@RestController
@RequestMapping("/sms")
@Api("短信服务")
@Slf4j
public class SmsController {
    @Value("${phone.provider.url}")
    private String phoneProviderUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ISendDetailsService sendDetailsService;

    @Autowired
    private ISendLogService sendLogService;


    @GetMapping("/send")
    public Result<Object> send(@RequestBody SmsRequest request, HttpServletRequest servletRequest) {
        Object userInfo = servletRequest.getSession().getAttribute("userInfo");
        User loginUser = new User();
        if (!(userInfo instanceof User)) {
            return Result.success("您尚未登录！");
        }

        List<SendDetails> sendDetails = Lists.newArrayList();

        List<String> phones = request.getPhones();
        String content = request.getContent();

        // 生成批次号
        long batchNO = System.currentTimeMillis();

        Iterator<String> iterator = phones.iterator();
        while(iterator.hasNext()){
            String phone = iterator.next();
            // 手机号格式不匹配
            if(!PhoneUtil.checkPhone(phone)){
                // @todo 错误数据存在数据库
                sendDetails.add(new SendDetails().setBatch(String.valueOf(batchNO)).setPhone(phone)
                        .setStatus("fail").setMsg("手机号格式错误").setCa(loginUser.getCode()).setCt(LocalDateTime.now()));
                iterator.remove();
            }
        }

        ResponseEntity<SmsResponse> send = SMSUtils.send(phones, content);
        if (send.getStatusCodeValue() != 200) {
            return Result.fail("调用短信接口失败!");
        }

        SendLog sendLog = new SendLog().setBatch(String.valueOf(batchNO))
                .setCa(loginUser.getCode())
                .setCt(LocalDateTime.now()).setContent(content);
        SmsResponse data = send.getBody();
        String respDesc = data.getRespDesc();
        if ("0000".equals(data.getRespCode())) {
            sendLogService.save(sendLog.setStatus("success"));
            List<FailPhone> failList = data.getFailList();

            // 调用接口成功，但是有发送失败的
            if (failList != null && failList.isEmpty()) {
                failList.forEach(i -> {
                    phones.remove(i.getPhone());
                    // @todo 错误数据存在数据库
                    sendDetails.add(new SendDetails().setBatch(String.valueOf(batchNO)).setPhone(i.getPhone())
                            .setStatus("fail").setMsg(i.getRespCode()).setCa(loginUser.getCode()).setCt(LocalDateTime.now()));

                });
            }

            // 所有错误的数据已经清除，保存正确的数据
            phones.forEach(i -> {
                sendDetails.add(new SendDetails().setBatch(String.valueOf(batchNO)).setPhone(i)
                        .setStatus("success").setMsg("发送成功").setCa(loginUser.getCode()).setCt(LocalDateTime.now()));
            });
            sendDetailsService.saveBatch(sendDetails);
            return Result.success("调用短信接口成功!");
        } else {
            phones.forEach(i -> {
                sendDetails.add(new SendDetails().setBatch(String.valueOf(batchNO)).setPhone(i)
                        .setStatus("fail").setMsg(respDesc).setCa(loginUser.getCode()).setCt(LocalDateTime.now()));
            });
            sendDetailsService.saveBatch(sendDetails);
            sendLogService.save(sendLog.setStatus("fail"));
            return Result.fail("调用短信接口失败!");
        }
    }


    private String getPhoneProvider(String phone) {
        String url = phoneProviderUrl + phone;
        ResponseEntity<Phone> entity = restTemplate.getForEntity(url, Phone.class);
        try {
            if (entity.getStatusCodeValue() == 200) {
                log.info("成功");
                Phone body = entity.getBody();
                return body.getProviderName();
            } else {
                throw new Exception("调用手机号运营商接口失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }
}
