package com.danmi.sms.controller;


import com.danmi.sms.common.vo.Result;
import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.SendLog;
import com.danmi.sms.entity.request.SendDetailRequest;
import com.danmi.sms.entity.request.SendLogRequest;
import com.danmi.sms.service.ISendLogService;
import com.danmi.sms.vo.SendDetailsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chunqiu
 * @since 2021-11-30
 */
@RestController
@RequestMapping("/send-log")
public class SendLogController {

    @Autowired
    private ISendLogService sendLogService;
    
    /**
     * 发送概况
     * @return
     */
    @GetMapping("general")
    public Result<Object> general(SendLogRequest request) {
        PageDTO<SendLog> result = sendLogService.general(request);
        return Result.success(result);
    }

    /**
     * 状态分析
     * @return
     */
    @GetMapping("status-analysis")
    public Result<Object> statusAnalysis(SendDetailRequest request) {
        List<SendDetailsVO> sendDetailsVOS = sendLogService.statusAnalysis(request);
        return Result.success(sendDetailsVOS);
    }

    /**
     * 发送详情
     * @return
     */
    @GetMapping("send-details")
    public Result<Object> sendDetails(SendDetailRequest request) {
        List<SendDetailsVO> sendDetailsVOS = sendLogService.sendDetails(request);
        return Result.success(sendDetailsVOS);
    }

    /**
     * 回复记录
     * @return
     */
    @GetMapping("reply-record")
    public Result<Object> replyRecord(SendDetailRequest request) {
        List<SendDetailsVO> sendDetailsVOS = sendLogService.replyRecord(request);
        return Result.success(sendDetailsVOS);
    }


    /**
     * 数据统计
     * @return
     */
    @GetMapping("data-statistics")
    public Result<Object> dataStatistics(SendDetailRequest request) {
        List<SendDetailsVO> sendDetailsVOS = sendLogService.dataStatistics(request);
        return Result.success(sendDetailsVOS);
    }


}
