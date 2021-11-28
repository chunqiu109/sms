package com.danmi.sms.entity.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TemplateRequest {
    private String content;

    private String approveStatus;

    private Integer limit;

    private Integer page;
}
