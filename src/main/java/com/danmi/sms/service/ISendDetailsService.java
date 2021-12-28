package com.danmi.sms.service;

import com.danmi.sms.common.vo.Result;
import com.danmi.sms.entity.SendDetails;
import com.baomidou.mybatisplus.extension.service.IService;
import com.danmi.sms.entity.User;
import com.danmi.sms.entity.request.SendDetailRequest;
import com.danmi.sms.entity.request.SmsRequest;
import com.danmi.sms.vo.SendDetailsVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chunqiu
 * @since 2021-11-29
 */
public interface ISendDetailsService extends IService<SendDetails> {

    List<SendDetails> sendDetails(SendDetailRequest request);

    List<SendDetailsVO> replyRecord(SendDetailRequest request);

    Result phoneImport(SmsRequest request, User user) throws IOException;

    Result phoneImportByFile(MultipartFile file, User user) throws IOException;
}
