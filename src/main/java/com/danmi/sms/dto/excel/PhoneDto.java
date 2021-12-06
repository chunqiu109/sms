package com.danmi.sms.dto.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;


@Data
@Accessors(chain = true)
public class PhoneDto {

    @Excel(name = "电话")
    private String phone;
}
