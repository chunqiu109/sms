package com.danmi.sms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.danmi.sms.entity.Menu;
import com.danmi.sms.mapper.MenuMapper;
import com.danmi.sms.service.IMenuService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chunqiu
 * @since 2021-11-19
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {


    /**
     *
     * @param ids
     * @param option 1 只筛选菜单
     * @return
     */
    public List<Menu> findTree(List<Integer> ids, int option) {
        List<Menu> allMenu;
        //查询所有菜单
        if (ids != null) {
            allMenu = this.listByIds(ids);
        } else {
            allMenu = this.list();
        }

        if (option == 1) {
            allMenu = allMenu.stream().filter(i -> i.getIsMenu().equals(1)).collect(Collectors.toList());
        }

        //根节点
        List<Menu> rootMenu = new ArrayList<>();
        for (Menu nav : allMenu) {
            if (nav.getParentId().equals(0)) { //父节点是0的，为根节点。
                rootMenu.add(nav);
            }
        }
        /* 根据Menu类的order排序 */
        Collections.sort(rootMenu, order());
        //为根菜单设置子菜单，getClild是递归调用的
        for (Menu nav : rootMenu) {
            /* 获取根节点下的所有子节点 使用getChild方法*/
            List<Menu> childList = getChild(nav.getId(), allMenu);
            nav.setChildren(childList);//给根节点设置子节点
        }
        /**
         * 输出构建好的菜单数据。
         *
         */

        return rootMenu;
    }


    /**
     * 获取子节点
     *
     * @param id      父节点id
     * @param allMenu 所有菜单列表
     * @return 每个根节点下，所有子菜单列表
     */
    public List<Menu> getChild(Integer id, List<Menu> allMenu) {
        //子菜单
        List<Menu> childList = new ArrayList<Menu>();
        for (Menu nav : allMenu) {
            // 遍历所有节点，将所有菜单的父id与传过来的根节点的id比较
            //相等说明：为该根节点的子节点。
            if (nav.getParentId().equals(id)) {
                childList.add(nav);
            }
        }
        //递归
        for (Menu nav : childList) {
            nav.setChildren(getChild(nav.getId(), allMenu));
        }
        Collections.sort(childList, order()); //排序
        //如果节点下没有子节点，返回一个空List（递归退出）
        if (childList.size() == 0) {
            return new ArrayList<Menu>();
        }
        return childList;
    }

    /*
     * 排序,根据order排序
     */
    public Comparator<Menu> order() {
        Comparator<Menu> comparator = new Comparator<Menu>() {
            @Override
            public int compare(Menu o1, Menu o2) {
                if (o1.getOrderNum() != o2.getOrderNum()) {
                    return o1.getOrderNum() - o2.getOrderNum();
                }
                return 0;
            }
        };
        return comparator;
    }
}
