package com.danmi.sms.entity.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Request {
    private Integer limit;

    private Integer page;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
