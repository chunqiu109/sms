package com.danmi.sms.entity;

import lombok.Data;

@Data
public class Reply {
    private String MOPort;
    private String phone;
    private String content;
    private String MOTime;
    private long timestamp;
    private String sig;
}
