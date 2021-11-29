package com.danmi.sms.exception;

import com.danmi.sms.dto.BaseResponseBody;
import com.danmi.sms.enums.ExceptionEnum;
import lombok.Data;

/**
 * @author Rechel
 */

@Data
public class BusinessException extends HttpException {

    private static final String DEFAULT_TYPE = "BusinessException";
    private static final String DEFAULT_MSG = ExceptionEnum.BUSINESS.getReasonPhrase();

    private BaseResponseBody body;

    public BusinessException() {
        this.type = DEFAULT_TYPE;
        this.message = DEFAULT_MSG;
    }


    public BusinessException(BaseResponseBody body) {
        this.type = DEFAULT_TYPE;
        this.message = DEFAULT_MSG;
        this.body = body;
    }
}
