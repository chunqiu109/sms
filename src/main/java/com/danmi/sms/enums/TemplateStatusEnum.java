package com.danmi.sms.enums;


public enum TemplateStatusEnum {
    // 使用中
    USE("use"),
    // 已废弃
    DISCARD("discard");

    private String status;

    TemplateStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
