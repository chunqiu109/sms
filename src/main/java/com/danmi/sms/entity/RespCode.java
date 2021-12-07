package com.danmi.sms.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RespCode {
    private String respCode;
}
