package com.danmi.sms.vo;

import lombok.Data;

@Data
public class ReplyVO {
    private String MOPort;
    private String phone;
    private String content;
    private String MOTime;
    private long timestamp;
    private String sig;
}
