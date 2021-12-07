package com.danmi.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.danmi.sms.common.vo.Result;
import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.FailPhone;
import com.danmi.sms.entity.SendDetails;
import com.danmi.sms.entity.SendLog;
import com.danmi.sms.entity.User;
import com.danmi.sms.entity.request.SendDetailRequest;
import com.danmi.sms.entity.request.SendLogRequest;
import com.danmi.sms.entity.request.SmsRequest;
import com.danmi.sms.entity.response.SmsResponse;
import com.danmi.sms.enums.SmsSendStatusEnum;
import com.danmi.sms.mapper.SendLogMapper;
import com.danmi.sms.service.ISendDetailsService;
import com.danmi.sms.service.ISendLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.danmi.sms.utils.BigDecimalUtil;
import com.danmi.sms.utils.SMSUtils;
import com.danmi.sms.utils.UserUtils;
import com.danmi.sms.vo.SendDetailsVO;
import com.danmi.sms.vo.SendLogFailVO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chunqiu
 * @since 2021-11-30
 */
@Service
@Slf4j
public class SendLogServiceImpl extends ServiceImpl<SendLogMapper, SendLog> implements ISendLogService {

    @Autowired
    private SendLogMapper sendLogMapper;
    @Autowired
    private ISendDetailsService sendDetailsService;
    @Autowired
    private UserUtils userUtils;

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

        LocalDate endTime = request.getEndTime();

        if (!Objects.isNull(request.getEndTime())) {
            request.setEndTime(endTime.plusDays(1L));
        }

        LambdaQueryWrapper<SendLog> wrapper = Wrappers.<SendLog>lambdaQuery().gt(!Objects.isNull(request.getStartTime()), SendLog::getCt, request.getStartTime())
                .le(!Objects.isNull(endTime), SendLog::getCt, request.getEndTime())
//                .le(false, SendLog::getCt, request.getEndTime().plusDays(1L))
                .eq(StringUtils.hasLength(request.getStatus()), SendLog::getStatus, request.getStatus())
                .like(StringUtils.hasLength(request.getContent()), SendLog::getContent, request.getContent());

        IPage<SendLog> data = sendLogMapper.selectPage(page, wrapper);
        User user = userUtils.getUser();
        List<SendLog> collect = data.getRecords().stream().filter(i -> i.getCa().substring(0, user.getCode().length() + 1).equals(user.getCode())).collect(Collectors.toList());
        collect.stream().forEach(i -> {
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

        data.setRecords(collect);

        return new PageDTO<>(data);
    }

    @Override
    public SendLogFailVO statusAnalysis(SendDetailRequest request, User user) {

        LocalDate endTime = request.getEndTime();

        if (!Objects.isNull(request.getEndTime())) {
            request.setEndTime(endTime.plusDays(1L));
        }

        LambdaQueryWrapper<SendLog> wrapper = Wrappers.<SendLog>lambdaQuery().gt(!Objects.isNull(request.getStartTime()), SendLog::getCt, request.getStartTime())
                .le(!Objects.isNull(endTime), SendLog::getCt, request.getEndTime());

        List<SendLog> list = list(wrapper);
        list = list.stream().filter(i -> i.getCa().substring(0, user.getCode().length()+1).equals(user.getCode())).collect(Collectors.toList());
        SendLogFailVO sendLogFailVO = new SendLogFailVO();
        int total = list.size();
        long failCount = list.stream().filter(i -> "fail".equals(i.getStatus())).count();
        if (total == 0) {
            sendLogFailVO.setCount(0);
            sendLogFailVO.setRate(BigDecimal.ZERO);
        } else {
            BigDecimal rate = BigDecimalUtil.divideRound(new BigDecimal(failCount), new BigDecimal(total), 2);
            sendLogFailVO.setCount((int) failCount).setRate(rate);
        }

        return sendLogFailVO;
    }

    @Override
    public List<SendDetailsVO> dataStatistics(SendDetailRequest request) {
        return null;
    }

    @Override
    public Result send(User loginUser, SmsRequest request) {

        List<SendDetails> sendDetails = Lists.newArrayList();

        String batchNO = request.getBatchNO();
        List<SendDetails> sendDetailsList = sendDetailsService.list(Wrappers.<SendDetails>lambdaQuery().eq(SendDetails::getBatch, batchNO));
        List<String> phones = sendDetailsList.stream().map(i -> i.getPhone()).collect(Collectors.toList());

        // list生成map phone为key,id为value
        Map<String, Integer> maps = sendDetailsList.stream().collect(Collectors.toMap(SendDetails::getPhone, SendDetails::getId, (key1, key2) -> key2));
//        List<String> phones = request.getPhones();
        String content = request.getContent();

        // 生成批次号
//        long batchNO = System.currentTimeMillis();


//        Iterator<String> iterator = phones.iterator();
//        while(iterator.hasNext()){
//            String phone = iterator.next();
//            // 手机号格式不匹配
//            if(!PhoneUtil.checkPhone(phone)){
//                // @todo 错误数据存在数据库
//                sendDetails.add(new SendDetails().setBatch(batchNO).setPhone(phone)
//                        .setStatus("fail").setMsg("手机号格式错误").setCa(loginUser.getCode()).setCt(LocalDateTime.now()));
//                iterator.remove();
//            }
//        }

        String sendTime;
        if (request.getType().equals(1)) { // 立即发送
            sendTime = "now";
            Result result = sendAndProcessService(sendTime, request, loginUser, phones, maps,null);
            return result;

        } else if (request.getType().equals(2)){ // 定时发送，先不发送，只更新log和detail表的状态
            sendTime = request.getSentTime();
            String status = SmsSendStatusEnum.TIME_SEND.getStatus();
            // 保存详情
            boolean detailFlag = sendDetailsService.update(Wrappers.<SendDetails>lambdaUpdate().eq(SendDetails::getBatch, batchNO).set(SendDetails::getStatus, status));
            // 保存日志
            SendLog sendLog = new SendLog().setBatch(batchNO)
                    .setCa(loginUser.getCode())
                    .setType(request.getType())
                    .setStatus(status)
                    .setSendTime(sendTime)
                    .setCt(LocalDateTime.now()).setContent(content);
            boolean logFlag = this.save(sendLog);
            if (detailFlag && logFlag) {
                return Result.fail("设置成功！");
            }

        } else {
            return Result.fail("类型传值错误！");
        }
        return Result.fail("设置失败！");
    }


    public Result sendAndProcessService(String sendTime, SmsRequest request, User loginUser, List<String> phones, Map<String, Integer> maps, Integer logId) {
        String content = request.getContent();
        String batchNO = request.getBatchNO();

        List<SendDetails> sendDetails = Lists.newArrayList();

        ResponseEntity<SmsResponse> send = SMSUtils.send(phones, content);
        if (send.getStatusCodeValue() != 200) {
            return Result.fail("调用短信接口失败!");
        }

        SendLog sendLog = new SendLog().setBatch(batchNO)
                .setCa(loginUser.getCode())
                .setType(request.getType())
                .setSendTime(sendTime)
                .setCt(LocalDateTime.now()).setContent(content);

        if (logId != null) {
            sendLog.setId(logId);
        }

        SmsResponse data = send.getBody();
        String respDesc = data.getRespDesc();
        if ("0000".equals(data.getRespCode())) {

            this.saveOrUpdate(sendLog.setStatus("success").setSmsId(data.getSmsId()));
            List<FailPhone> failList = data.getFailList();

            // 调用接口成功，但是有发送失败的
            if (failList != null && failList.isEmpty()) {
                failList.forEach(i -> {
                    phones.remove(i.getPhone());
                    // @todo 错误数据更新
                    Integer id = maps.get(i.getPhone());
                    sendDetails.add(new SendDetails().setId(id).setBatch(batchNO).setPhone(i.getPhone())
                            .setStatus("fail").setMsg(i.getRespCode()).setCa(loginUser.getCode()).setCt(LocalDateTime.now()));

                });
            }

            // 所有错误的数据已经清除，保存正确的数据
            phones.forEach(i -> {
                Integer id = maps.get(i);
                sendDetails.add(new SendDetails().setId(id).setBatch(batchNO).setPhone(i)
                        .setStatus("success").setMsg("发送成功").setCa(loginUser.getCode()).setCt(LocalDateTime.now()));
            });
            sendDetailsService.updateBatchById(sendDetails);
            return Result.success("调用短信接口成功!");
        } else {
            phones.forEach(i -> {
                Integer id = maps.get(i);
                sendDetails.add(new SendDetails().setId(id).setBatch(batchNO).setPhone(i)
                        .setStatus("fail").setMsg(respDesc).setCa(loginUser.getCode()).setCt(LocalDateTime.now()));
            });
            sendDetailsService.updateBatchById(sendDetails);
            this.saveOrUpdate(sendLog.setStatus("fail"));
            return Result.fail("调用短信接口失败!");
        }
    }
}
