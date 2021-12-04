package com.danmi.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.danmi.sms.entity.Menu;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chunqiu
 * @since 2021-11-19
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> findTree(List<Integer> list, int option);
}
