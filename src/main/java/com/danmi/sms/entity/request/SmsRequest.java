package com.danmi.sms.entity.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SmsRequest extends Request{

    /**
     * 手动输入的手机号，传了文件就不需要填写此项
     */
    private List<String> phones;
    /**
     * 短信内容
     */
    private String content;
    /**
     * 1-立刻发送，2-定时发送
     */
    private Integer type;
    /**
     * 发送时间：立即发送不填写
     */
    private String sentTime;
    /**
     * 批次号，在导入文件的时候不需要填写
     */
    private String batchNO;

//    private String code;

}
