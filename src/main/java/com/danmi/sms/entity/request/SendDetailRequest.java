package com.danmi.sms.entity.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SendDetailRequest  extends Request{

    private Integer type;

    private String status;

    /**
     * 运营商
     */
    private String providerName;

    private String phone;
}
