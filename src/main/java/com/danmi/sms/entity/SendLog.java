package com.danmi.sms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2021-11-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SendLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String ca;

    private LocalDateTime ct;

    /**
     * 批次号
     */
    private String batch;

    private String smsId;

    private Integer type;

    private String sendTime;

    private String content;

    /**
     * 发送状态
     */
    private String status;

    /**
     * 每个批次号码数量
     */
    @TableField(exist = false)
    private Integer phoneCount;

    /**
     *计费条数
     */
    @TableField(exist = false)
    private Integer billingNumber;


}
