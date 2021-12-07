package com.danmi.sms.entity;

import java.util.List;

public class SmsResult {
    private List<SendStatus> smsResult;
}





class SendStatus {

    private String smsId;
    private String phone;
    private String respCode;
    private int status;
    private String respMessage;
    private String receiveTime;
    private Integer chargingNum;
}
