package com.danmi.sms.enums;

public enum ExceptionEnum {

    UNAUTHORIZED("unauthorized", "Authorization token is invalid."),
    BUSINESS("External interface call exception", "External interface call exception."),
    PERMISSION_DENIED("Permission Denied", "Permission denied."),
    NOT_FOUND("Not Found", "The specified resource does not found."),
    METHOD_NOT_ALLOWED("Method Not Allowed", "The specified resource method is not supported for this request."),
    VALIDATION_FAILED("Validation Failed", "Validation failed."),
    INVALID_ARGUMENT("Invalid Argument", "An invalid value was specified for one of the Argument."),
    MISSING_FIELD("Missing Field", "A required parameter was not specified for this request."),
    MISSING_DATA("Missing Data", "The specified resource does not exists."),
    ALREADY_EXISTS("Already Exists", "Another resource has the same value as this field."),
    INTERNAL_SERVER_ERROR("Internal Server Error", "internal_server_error");

    private final String value;

    private final String reasonPhrase;

    ExceptionEnum(String value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public String getValue() {
        return value;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

}
