package com.danmi.sms.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SendLogFailVO {
    // 失败数量
    private Integer count;
    // 失败占比
    private BigDecimal rate;
    // 失败原因
//    private Integer reason;
}
