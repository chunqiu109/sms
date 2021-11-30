package com.danmi.sms.service;

import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.SendLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.danmi.sms.entity.request.SendDetailRequest;
import com.danmi.sms.entity.request.SendLogRequest;
import com.danmi.sms.vo.SendDetailsVO;

import java.util.List;

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

    List<SendDetailsVO> statusAnalysis(SendDetailRequest request);

    List<SendDetailsVO> sendDetails(SendDetailRequest request);

    List<SendDetailsVO> replyRecord(SendDetailRequest request);

    List<SendDetailsVO> dataStatistics(SendDetailRequest request);

}
