package com.danmi.sms.entity.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Request {
    private Integer limit;

    private Integer page;

    private LocalDate startTime;

    private LocalDate endTime;
}
