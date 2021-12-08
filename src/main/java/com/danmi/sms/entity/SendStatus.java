package com.danmi.sms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class SendStatus  implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String smsId;
    private String phone;
    private String respCode;
    private int status;
    private String respMessage;
    private String receiveTime;
    private Integer chargingNum;
}
