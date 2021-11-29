package com.danmi.sms.dto;

import lombok.Data;

@Data
public class BaseErrors<T> {

    private String type;

    private String description;

    private String field;

    private T data;

    public BaseErrors(){}
    public BaseErrors(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public BaseErrors(String type, String description, String field) {
        this.type = type;
        this.description = description;
        this.field = field;
    }
}
