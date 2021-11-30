package com.danmi.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.Role;
import com.danmi.sms.entity.SendDetails;
import com.danmi.sms.entity.SendLog;
import com.danmi.sms.entity.request.SendDetailRequest;
import com.danmi.sms.entity.request.SendLogRequest;
import com.danmi.sms.enums.SmsSendStatusEnum;
import com.danmi.sms.mapper.SendDetailsMapper;
import com.danmi.sms.mapper.SendLogMapper;
import com.danmi.sms.service.ISendDetailsService;
import com.danmi.sms.service.ISendLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.danmi.sms.utils.DateUtils;
import com.danmi.sms.vo.SendDetailsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chunqiu
 * @since 2021-11-30
 */
@Service
public class SendLogServiceImpl extends ServiceImpl<SendLogMapper, SendLog> implements ISendLogService {

    @Autowired
    private SendLogMapper sendLogMapper;
    @Autowired
    private ISendDetailsService sendDetailsService;

    @Override
    public PageDTO<SendLog> general(SendLogRequest request) {
//        String startTime = request.getStartTime();
//        String endTime = request.getEndTime();
        Integer pageNum = request.getPage();
        Integer pageSize = request.getLimit();
        if (pageSize==null || pageSize.equals(0)) {
            pageSize = 10;
        }
        if (pageNum==null || pageNum.equals(0)) {
            pageNum = 1;
        }
        IPage<SendLog> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<SendLog> wapper = Wrappers.<SendLog>lambdaQuery().gt(!Objects.isNull(request.getStartTime()), SendLog::getCt, request.getStartTime())
                .le(!Objects.isNull(request.getEndTime()), SendLog::getCt, request.getEndTime().plusDays(1L))
                .eq(StringUtils.hasLength(request.getStatus()), SendLog::getStatus, request.getStatus())
                .like(StringUtils.hasLength(request.getContent()), SendLog::getContent, request.getContent());

        IPage<SendLog> data = sendLogMapper.selectPage(page, wapper);
        data.getRecords().stream().forEach(i -> {
            int count = sendDetailsService.count(Wrappers.<SendDetails>lambdaQuery().eq(SendDetails::getBatch, i.getBatch()));

            int successCount = sendDetailsService.count(Wrappers.<SendDetails>lambdaQuery().eq(SendDetails::getBatch, i.getBatch())
                    .eq(SendDetails::getStatus, SmsSendStatusEnum.SUCCESS.getStatus()));

            i.setPhoneCount(count);
            int length = i.getContent().length();
            int num = 0;
            if (length%67 == 0) {
                num = length/67;
            } else {
                num = length/67+1;
            }
            i.setBillingNumber(successCount*num);
        });

        return new PageDTO<>(data);
    }

    @Override
    public List<SendDetailsVO> statusAnalysis(SendDetailRequest request) {
        return null;
    }

    @Override
    public List<SendDetailsVO> sendDetails(SendDetailRequest request) {
        return null;
    }

    @Override
    public List<SendDetailsVO> replyRecord(SendDetailRequest request) {
        return null;
    }

    @Override
    public List<SendDetailsVO> dataStatistics(SendDetailRequest request) {
        return null;
    }
}
