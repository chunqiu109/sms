package com.danmi.sms.exception;

import com.danmi.sms.enums.ExceptionEnum;
import lombok.Data;

@Data
public class HttpException extends Exception {

    private static final String DEFAULT_TYPE = "HttpException";
    private static final String DEFAULT_MSG = ExceptionEnum.INTERNAL_SERVER_ERROR.getReasonPhrase();

    String type;
    String message;

    public HttpException() {
        this.type = DEFAULT_TYPE;
        this.message = DEFAULT_MSG;
    }

    public HttpException(String message) {
        this.type = DEFAULT_TYPE;
        this.message = message;
    }

    public HttpException(String type, String message) {
        this.type = type;
        this.message = message;
    }

}
