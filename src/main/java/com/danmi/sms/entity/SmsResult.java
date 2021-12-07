package com.danmi.sms.entity;

import lombok.Data;

import java.util.List;

@Data
public class SmsResult {
    private List<SendStatus> smsResult;
}





//class SendStatus {
//
//    private String smsId;
//    private String phone;
//    private String respCode;
//    private int status;
//    private String respMessage;
//    private String receiveTime;
//    private Integer chargingNum;
//}
