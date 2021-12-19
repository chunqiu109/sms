package com.danmi.sms.entity;

import lombok.Data;

@Data
public class AuthList {
    /**
     * 查看权限
     */
    private Boolean query;
    /**
     * 删除修改新增权限
     */
    private Boolean option;
    /**
     * 导出权限
     */
    private Boolean export;
}
