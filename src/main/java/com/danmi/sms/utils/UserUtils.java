package com.danmi.sms.utils;

import com.danmi.sms.entity.Role;
import com.danmi.sms.entity.User;
import com.danmi.sms.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Service
public class UserUtils {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private IRoleService roleService;

    public User getUser() {
        Object userInfo = request.getSession().getAttribute("userInfo");
//        Object userInfo2 = getSession().getAttribute("userInfo");
        User loginUser;
        if (userInfo instanceof User) {
            loginUser = (User) userInfo;
            return loginUser;
        }
        return null;
    }

    public boolean isSystemAdmin() {
        User user = getUser();
        Role role = roleService.getById(user.getRoleId());
        Boolean isSystemAdmin = "system_admin".equals(role.getCode());
        return isSystemAdmin;
    }

    /**
     * 获取request
     * @return
     */
    public static HttpServletRequest getRequest(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes==null? null : requestAttributes.getRequest();
    }
    /**
     * 获取session
     * @return
     */
    public static HttpSession getSession(){
        return getRequest().getSession(false);
    }

}
