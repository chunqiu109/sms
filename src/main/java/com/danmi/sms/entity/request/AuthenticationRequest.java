package com.danmi.sms.entity.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AuthenticationRequest {

    private String company;

    /**
     * 法人
     */
    private String legalPerson;

    /**
     * 统一社会信用代码或注册号
     */
    private String creditCode;

    private String approveStatus;

    private Integer limit;

    private Integer page;
}
