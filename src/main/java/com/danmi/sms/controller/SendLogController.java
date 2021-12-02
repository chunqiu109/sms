package com.danmi.sms.controller;


import com.danmi.sms.common.vo.Result;
import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.SendLog;
import com.danmi.sms.entity.User;
import com.danmi.sms.entity.request.SendDetailRequest;
import com.danmi.sms.entity.request.SendLogRequest;
import com.danmi.sms.service.ISendLogService;
import com.danmi.sms.utils.UserUtils;
import com.danmi.sms.vo.SendDetailsVO;
import com.danmi.sms.vo.SendLogFailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 发送概况
 *
 * @author chunqiu
 * @since 2021-11-30
 */
@RestController
@RequestMapping("/send-log")
public class SendLogController {

    @Autowired
    private ISendLogService sendLogService;
    @Autowired
    private UserUtils userUtils;
    
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
        User user = userUtils.getUser();
        SendLogFailVO sendLogFailVO = sendLogService.statusAnalysis(request, user);
        return Result.success(sendLogFailVO);
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
