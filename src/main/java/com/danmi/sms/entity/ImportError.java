package com.danmi.sms.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author chunqiu
 * @since 2021-08-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ImportError implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String phone;

    private String cusName;

    private String ca;

    private Date ct;


}
