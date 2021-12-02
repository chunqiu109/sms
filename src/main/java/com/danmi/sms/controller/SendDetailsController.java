package com.danmi.sms.controller;


import com.danmi.sms.common.vo.Result;
import com.danmi.sms.entity.SendDetails;
import com.danmi.sms.entity.request.SendDetailRequest;
import com.danmi.sms.service.ISendDetailsService;
import com.danmi.sms.vo.SendDetailsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 发送详情
 *
 * @author chunqiu
 * @since 2021-11-29
 */
@RestController
@RequestMapping("/send-details")
public class SendDetailsController {


    @Autowired
    private ISendDetailsService sendDetailsService;

    /**
     * 发送详情
     * @return
     */
    @GetMapping("list")
    public Result<Object> sendDetails(SendDetailRequest request) {
        List<SendDetails> sendDetails = sendDetailsService.sendDetails(request);
        return Result.success(sendDetails);
    }

    /**
     * 回复记录
     * @return
     */
    @GetMapping("reply-record")
    public Result<Object> replyRecord(SendDetailRequest request) {
        List<SendDetailsVO> sendDetailsVOS = sendDetailsService.replyRecord(request);
        return Result.success(sendDetailsVOS);
    }
}
