package com.danmi.sms.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class BaseResponseBody<T>  {

    private Integer code;

    private String message;

    private T data;

    private List<BaseErrors<T>> errors;

    public BaseResponseBody(){}
    public BaseResponseBody(T data) {
        this.code = 0;
        this.message = "";
        this.data = data;
    }

    public BaseResponseBody(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResponseBody(Integer code, String message, List<BaseErrors<T>> errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
    }
}


