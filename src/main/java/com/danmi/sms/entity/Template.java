package com.danmi.sms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2021-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Template implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String ca;

    private LocalDateTime ct;

    /**
     * 内容
     */
    private String content;
    /**
     * 签名id
     */
    private Integer sign;
    /**
     * 模板状态
     */
    private String status;
    /**
     * 审批状态
     */
    private String approveStatus;


}
