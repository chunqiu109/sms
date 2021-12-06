package com.danmi.sms.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ImportPhoneVO {
    /**
     * 重复的数量
     */
    private int repeatedNum;

    /**
     * 成功的数量
     */
    private int successNum;

    /**
     * 格式错误的数量
     */
    private int formatErrNum;

    /**
     * 总共的数量
     */
    private int totalNum;

    /**
     * 批次号
     */
    private String batch;
}
