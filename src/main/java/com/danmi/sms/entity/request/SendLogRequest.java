package com.danmi.sms.entity.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SendLogRequest extends Request{

    private Integer type;

    private String status;

    /**
     * 运营商
     */
//    private String providerName;
    /**
     * 内容
     */
    private String content;

    private String phone;
}
