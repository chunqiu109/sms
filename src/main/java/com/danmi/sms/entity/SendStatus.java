package com.danmi.sms.entity;

import lombok.Data;

@Data
public class SendStatus {

    private String smsId;
    private String phone;
    private String respCode;
    private int status;
    private String respMessage;
    private String receiveTime;
    private Integer chargingNum;
}
