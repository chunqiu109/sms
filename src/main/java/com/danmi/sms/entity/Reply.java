package com.danmi.sms.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author chunqiu
 * @since 2021-12-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Reply implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 回复时间
     */
    private LocalDateTime replyTime;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 回复内容
     */
    private String replyContent;


}
