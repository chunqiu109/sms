package com.danmi.sms.service;

import com.danmi.sms.dto.PageDTO;
import com.danmi.sms.entity.Menu;
import  com.danmi.sms.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.danmi.sms.entity.request.RoleRequest;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chunqiu
 * @since 2021-11-19
 */
public interface IRoleService extends IService<Role> {

    PageDTO<Role> listRolePage(RoleRequest role);

    List<Menu> getMenuList(Integer roleId);
}
