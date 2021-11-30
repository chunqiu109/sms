package com.danmi.sms.enums;


public enum SmsSendStatusEnum {
    // 提交失败
    SUBMIT_FAIL("submit_fail"),
    // 提交成功
    SUBMIT_SUCCESS("submit_success"),
    // 取消中
    CANCELING("canceling"),
    // 待审核
    APPROVING("approving"),
    // 未通过
    REJECT("reject"),
    // 待发送
    UNSEND("unSend"),
    // 定时发送
    TIME_SEND("time_send"),
    // 已完成
    SUCCESS("success"),
    // 已取消
    CANCELED("canceled");

    private String status;

    SmsSendStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
