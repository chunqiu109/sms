package com.danmi.sms.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * mybatis plus 自动填充字段值
 *
 * 自动填充问题暂时不做，2021-12.01
 */
@Slf4j
@Component
public class MybatisPlusFillConfig implements MetaObjectHandler {

    private final String updatedAt = "updatedAt";
    private final String updatedBy = "updatedBy";
    private final String createdAt = "createdAt";
    private final String createdBy = "createdBy";

    @Override
    public void insertFill(MetaObject metaObject) {
        final String isDeleted = "isDeleted";

        Date now = new Date();
//        String username = MySecurityFilter.getUsername();
        String username = "System";

        if (metaObject.hasSetter(createdAt)) {
            setFieldValByName(createdAt, now, metaObject);
        }
        if (metaObject.hasSetter(createdBy)) {
            setFieldValByName(createdBy, username, metaObject);
        }
        if (metaObject.hasSetter(updatedAt)) {
            setFieldValByName(updatedAt, now, metaObject);
        }
        if (metaObject.hasSetter(updatedBy)) {
            setFieldValByName(updatedBy, username, metaObject);
        }
//        if (metaObject.hasSetter(isDeleted)) {
//            if (ObjectUtils.isEmpty(getFieldValByName(isDeleted, metaObject))) {
//                setFieldValByName(isDeleted, DeleteStatus.NO, metaObject);
//            }
//        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        String username = "System";
        if (metaObject.hasSetter(updatedAt)) {
            setFieldValByName(updatedAt, new Date(), metaObject);
        }
        if (metaObject.hasSetter(updatedBy)) {
//            String username = MySecurityFilter.getUsername();
            setFieldValByName(updatedBy, username, metaObject);
        }
    }
}
