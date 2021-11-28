package com.danmi.sms.dto.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;


@Data
@Accessors(chain = true)
public class CustomerDto {

    @Excel(name = "id")
    private Long id;
    @Excel(name = "客户姓名")
    private String name;
    @Excel(name = "性别")
    private Integer sex;
    @Excel(name = "电话")
    private String phone;
    @Excel(name = "创建人")
    private String ca;
    @Excel(name = "创建时间", importFormat  = "yyyy-MM-dd HH:mm:ss")
    private Date ct;
}
