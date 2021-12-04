package com.danmi.sms.utils;

import com.danmi.sms.entity.response.SmsResponse;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Slf4j
public class SMSUtils {
    private static String accountId;
    private static String accountSid;
    private static String authToken;
    private static String smsApiUrl;

    @Value("${account.id}")
    public void setAccountId(String accountId) {
        SMSUtils.accountId = accountId;
    }

    @Value("${account.sid}")
    public void setAccountSid(String accountSid) {
        SMSUtils.accountSid = accountSid;
    }

    @Value("${auth.token}")
    public void setAuthToken(String authToken) {
        SMSUtils.authToken = authToken;
    }

    @Value("${sms.api.url}")
    public void setSmsApiUrl(String smsApiUrl) {
        SMSUtils.smsApiUrl = smsApiUrl;
    }


    private static RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static ResponseEntity<SmsResponse> send(List<String> phones, String content) {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, Object> params = getParam(phones, content);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(params, headers);

        ResponseEntity<SmsResponse> smsResponseResponseEntity = restTemplate.postForEntity(smsApiUrl, requestEntity, SmsResponse.class);
        return smsResponseResponseEntity;
    }

    public static MultiValueMap<String, Object> getParam(List<String> phones, String content) {

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
        // 时间戳
        long timestamp = System.currentTimeMillis();
        // 签名
        String sig = DigestUtils.md5Hex(accountSid + authToken + timestamp);
        log.info(timestamp + "------------" + sig);
        String phoneStr = Joiner.on(",").join(phones);


        StringBuilder sb = new StringBuilder();

        sb.append("accountSid").append("=").append(accountSid);

        params.add("accountSid", accountSid);
        params.add("to", phoneStr);
        params.add("smsContent", content);
        params.add("timestamp", timestamp);
        params.add("sig", sig);
        return params;
    }
}
