package com.danmi.sms.enums;


public enum SignApproveStatusEnum {
    // 待审批
    UN_APPROVE("unApprove"),
    // 通过
    APPROVED("approved"),
    // 拒绝
    REJECT("reject");

    private String status;

    SignApproveStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
