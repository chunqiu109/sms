package com.danmi.sms.entity.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SmsRequest extends Request{

    private List<String> phones;
    private String content;
    /**
     * 1-立刻发送，2-定时发送
     */
    private Integer type;
    private String sentTime;
    private String batchNO;

//    private String code;

}
