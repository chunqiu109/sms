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
public class Authentication implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String ca;

    private LocalDateTime ct;

    /**
     * 企业
     */
    private String company;

    /**
     * 法人
     */
    private String legalPerson;

    /**
     * 资质证件url
     */
    private String certification;

    /**
     * 统一社会信用代码或注册号
     */
    private String creditCode;

    private String approveStatus;

    /**
     * 拒绝原因
     */
    private String rejectReason;


}
