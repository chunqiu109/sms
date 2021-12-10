package com.danmi.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.danmi.sms.common.vo.Result;
import com.danmi.sms.entity.SendDetails;
import com.danmi.sms.entity.User;
import com.danmi.sms.entity.request.SendDetailRequest;
import com.danmi.sms.entity.request.SmsRequest;
import com.danmi.sms.enums.SmsSendStatusEnum;
import com.danmi.sms.mapper.SendDetailsMapper;
import com.danmi.sms.service.ISendDetailsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.danmi.sms.utils.PhoneUtil;
import com.danmi.sms.utils.UserUtils;
import com.danmi.sms.vo.ImportPhoneVO;
import com.danmi.sms.vo.SendDetailsVO;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chunqiu
 * @since 2021-11-29
 */
@Service
public class SendDetailsServiceImpl extends ServiceImpl<SendDetailsMapper, SendDetails> implements ISendDetailsService {
    @Autowired
    private UserUtils userUtils;

    @Override
    public List<SendDetails> sendDetails(SendDetailRequest request) {
        User user = userUtils.getUser();
        LocalDate endTime = request.getEndTime();

        if (!Objects.isNull(request.getEndTime())) {
            request.setEndTime(endTime.plusDays(1L));
        }
        LambdaQueryWrapper<SendDetails> wrapper = Wrappers.<SendDetails>lambdaQuery().gt(!Objects.isNull(request.getStartTime()), SendDetails::getCt, request.getStartTime())
                .le(!Objects.isNull(endTime), SendDetails::getCt, request.getEndTime())
//                .eq(!Objects.isNull(request.getType()), SendDetails::getType, request.getEndTime())
                .eq(!Objects.isNull(request.getStatus()), SendDetails::getStatus, request.getStatus());
        List<SendDetails> list = list(wrapper);

        // 过滤当前登录人自己创建的
        if (!userUtils.isSystemAdmin()) {
            list = list.stream().filter(i -> i.getCa().startsWith(user.getCode())).collect(Collectors.toList());
        }

        return list;
    }

    @Override
    public List<SendDetailsVO> replyRecord(SendDetailRequest request) {
        return null;
    }

    @Override
    public Result phoneImport(MultipartFile file, SmsRequest request) throws IOException {

//        if (!((!ObjectUtils.isEmpty(file) && !file.isEmpty()) || (!ObjectUtils.isEmpty(request.getPhones()) && !request.getPhones().isEmpty())))
        List<String> phones = Lists.newArrayList();

        // 以文件格式传入手机号
        if (!ObjectUtils.isEmpty(file) && !file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            int index = originalFilename.lastIndexOf('.') + 1;//获取地址.的前面的数字，从0开始
            String type = originalFilename.substring(index);//从地址.开始截取后缀

            if (!("txt".equals(type) || "xlsx".equals(type))) {
                return Result.fail("文件格式错误!");
            }

            if ("xlsx".equals(type)) {
                phones = parseExcel(file);
            } else if ("txt".equals(type)) {
                phones = parseTxt(file);
            }
        // 手动输入手机号
        } else if (!ObjectUtils.isEmpty(request.getPhones()) && !request.getPhones().isEmpty()){
            phones = request.getPhones();
        }

        // 生成批次号
        String batch = String.valueOf(System.currentTimeMillis());


        // 对手机号格式校验，去重
        ImportPhoneVO importPhoneVO = new ImportPhoneVO();
        int total = phones.size();

        // 格式错误的数量
        long formatErrNum = phones.stream().filter(i -> !PhoneUtil.checkPhone(i)).count();
        long num = phones.stream().distinct().count();
        long repeatedNum = total - num;
        long successNum = phones.stream().filter(i -> PhoneUtil.checkPhone(i)).distinct().count();
        importPhoneVO.setTotalNum(total)
                .setRepeatedNum((int) repeatedNum)
                .setFormatErrNum((int) formatErrNum)
                .setSuccessNum((int) successNum)
                .setBatch(batch);

        User user = userUtils.getUser();

        List<String> successList = phones.stream().filter(i -> PhoneUtil.checkPhone(i)).distinct().collect(Collectors.toList());

        List<SendDetails> sendDetails = new ArrayList<>(successList.size());

        successList.stream().forEach(i -> {
            sendDetails.add(new SendDetails()
                    .setCt(LocalDateTime.now())
                    .setCa(user.getCode())
                    .setStatus(SmsSendStatusEnum.APPROVING.getStatus())
                    .setPhone(i).setBatch(batch));
        });

        boolean flag = saveBatch(sendDetails);
        if (flag) {
            return Result.success(importPhoneVO);
        } else {
            return Result.fail("导入失败！");
        }

    }

    private List<String> parseExcel(MultipartFile file) throws IOException {
        List<String> phones = Lists.newArrayList();

        Workbook wb = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = wb.getSheetAt(0);
        //拿到这表的总行数
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 0; i <= lastRowNum; i++) {
            //拿到每一行
            Row row = sheet.getRow(i);
            //拿到这一行的总列数
            short lastCellNum = row.getLastCellNum();
            for (int j = 0; j < lastCellNum; j++) {
                //拿到这一个格子与它的数据
                Cell cell = row.getCell(j);
                cell.setCellType(CellType.STRING);
                phones.add(cell.getStringCellValue());
            }
        }
        return phones;
    }

    private List<String> parseTxt(MultipartFile file) throws IOException {
        List<String> phones = Lists.newArrayList();

        // 适用于txt
        Reader reader = null;
        reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(reader);

        String line;
        while ((line = br.readLine()) != null) {
            // 一次读入一行数据
            if (line != null && line != "") {
                phones.add(line);
            }
        }
        reader.close();
        return phones;
    }

}
