package com.danmi.sms.controller;

import com.alibaba.fastjson.JSONObject;
import com.danmi.sms.common.vo.Result;
import com.danmi.sms.entity.Phone;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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


    @GetMapping("/send")
    public Result<Object> send(List<String> phones) {
        return Result.success("");
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
