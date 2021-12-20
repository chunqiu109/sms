package com.danmi.sms.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.danmi.sms.common.vo.Result;
import com.danmi.sms.entity.*;
import com.danmi.sms.entity.request.SmsRequest;
import com.danmi.sms.service.IReplyService;
import com.danmi.sms.service.ISendDetailsService;
import com.danmi.sms.service.ISendLogService;
import com.danmi.sms.service.ISendStatusService;
import com.danmi.sms.utils.DateUtils;
import com.danmi.sms.utils.UserUtils;
import com.danmi.sms.vo.ReplyVO;
import com.google.common.collect.Lists;
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
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Autowired
    private IReplyService replyService;

    @Autowired
    private ISendStatusService sendStatusService;

    @Autowired
    private UserUtils userUtils;


    @PostMapping("/send")
    public Result<Object> send(@RequestBody SmsRequest request, HttpServletRequest servletRequest) {
        Object userInfo = servletRequest.getSession().getAttribute("userInfo");

        if (!(userInfo instanceof User)) {
            return Result.fail("您尚未登录！");
        }

        if (ObjectUtils.isEmpty(request.getType()) || StringUtils.isEmpty(request.getContent())| StringUtils.isEmpty(request.getBatchNO())) {
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
    @PostMapping("/reply")
    public RespCode reply(@RequestBody ReplyVO reply) {
        log.info("接收用户回复: {}", JSONObject.toJSONString(reply));
//        当天发送给回复用户的手机号，当天超过3个企业发送的就取当天的，如果小于3个，就按时间倒序排序选择前三条数据，
//        用户只回复一次，但是记录企业的个数条数。是否排除数据？

        String todayDate = DateUtils.localDateToString(LocalDate.now());
        // 查询当天发送详情中给此手机号发送的企业
        String phone = reply.getPhone();
        List<SendDetails> sendDetails = sendDetailsService.list(Wrappers.<SendDetails>lambdaQuery().eq(SendDetails::getPhone, phone).orderByDesc(SendDetails::getCt));

        List<SendDetails> todaySendDetails = sendDetails.stream()
                .filter(i -> DateUtils.localDateTimeToString(i.getCt()).substring(0, 10).equals(todayDate))
                .collect(Collectors.toList());

        List<SendDetails> result =  Lists.newArrayList();
        List<Reply> replies = Lists.newArrayList();

        // 大于等于3就取当天的企业并保存
        if (!ObjectUtils.isEmpty(todaySendDetails)) {
            if (todaySendDetails.size() >= 3) {
                result = todaySendDetails;
            } else { // 如果小于3个，就按时间倒序排序选择前三条数据并保存
                result = sendDetails.subList(0, 3);
            }
        }

        result.stream().forEach(i -> {
            replies.add(new Reply()
                    .setReplyTime(DateUtils.timestampToLocalDateTime(reply.getTimestamp()))
                    .setPhone(phone)
                    .setCa(i.getCa())
                    .setReplyContent(reply.getContent()));
        });

        replyService.saveBatch(replies);
        return new RespCode().setRespCode("0000");
    }

    /**
     * 接收短信回执
     * @return
     */
    @PostMapping("/status")
    public RespCode status(@RequestBody SmsResult smsResult) {
        log.info("接收短信回执: {}", JSONObject.toJSONString(smsResult));
        List<SendStatus> smsResultList = smsResult.getSmsResult();

        // 保存全量数据
        sendStatusService.saveBatch(smsResultList);

        // 获取到了短信状态然后更新
        smsResultList.forEach(i -> {
            // 获取批次号
            SendLog sendLog = sendLogService.getOne(Wrappers.<SendLog>lambdaQuery().select(SendLog::getBatch).eq(SendLog::getSmsId, i.getSmsId()));
            if (ObjectUtils.isEmpty(sendLog)) {
                log.info("smsId >>> {} 在当前库并未保存！", i.getSmsId());
                return;
            }

            // 根据批次号和手机号修改状态
            sendDetailsService.update(Wrappers.<SendDetails>lambdaUpdate().set(SendDetails::getStatus, i.getStatus())
                    .set(SendDetails::getMsg, i.getRespCode())
                    .eq(SendDetails::getBatch, sendLog.getBatch())
                    .eq(SendDetails::getPhone, i.getPhone()));
        });

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
        User user = userUtils.getUser();
        if (ObjectUtils.isEmpty(user)) {
            return Result.fail("请先登录！");
        }
        Result result = sendDetailsService.phoneImport(file, request, user);

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
