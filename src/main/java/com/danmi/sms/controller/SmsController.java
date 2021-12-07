package com.danmi.sms.controller;

import com.alibaba.fastjson.JSONObject;
import com.danmi.sms.common.vo.Result;
import com.danmi.sms.entity.*;
import com.danmi.sms.entity.request.SmsRequest;
import com.danmi.sms.service.ISendDetailsService;
import com.danmi.sms.service.ISendLogService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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


    @PostMapping("/send")
    public Result<Object> send(@RequestBody SmsRequest request, HttpServletRequest servletRequest) {
        Object userInfo = servletRequest.getSession().getAttribute("userInfo");

        if (!(userInfo instanceof User)) {
            return Result.fail("您尚未登录！");
        }

        if (ObjectUtils.isEmpty(request.getType()) || StringUtils.isEmpty(request.getContent())) {
            return Result.fail("类型或者内容不能为空！");
        }

        if (request.getType().equals(2) && StringUtils.isEmpty(request.getSentTime())) {
            return Result.fail("定时发送必须设置发送时间！");
        }

        User loginUser = (User) userInfo;

        Result result = sendLogService.send(loginUser, request);

        return result;
    }

    /**
     * 接收用户回复
     * @return
     */
    @GetMapping("/reply")
    public RespCode reply(@RequestBody List<Reply> replyList) {
        log.info("接收用户回复: {}", JSONObject.toJSONString(replyList));
        return new RespCode().setRespCode("0000");
    }

    /**
     * 接收短信回执
     * @return
     */
    @GetMapping("/status")
    public RespCode status(@RequestBody List<SendStatus> sendStatuses) {
        log.info("接收短信回执: {}", JSONObject.toJSONString(sendStatuses));
        return new RespCode().setRespCode("0000");
    }

    /**
     * 批量导入手机号
     * @return
     */
    @PostMapping("/phone-import")
    public Result phoneImport(@RequestParam("file") MultipartFile file, SmsRequest request) throws Exception {
        if (!((!ObjectUtils.isEmpty(file) && !file.isEmpty()) || (!ObjectUtils.isEmpty(request.getPhones()) && !request.getPhones().isEmpty()))) {
            return Result.fail("请先上传文件或者手动输入手机号！");
        }

        Result result = sendDetailsService.phoneImport(file, request);

        return result;

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
