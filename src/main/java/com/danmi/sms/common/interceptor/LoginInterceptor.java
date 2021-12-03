package com.danmi.sms.common.interceptor;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info(">>>>>>>>>>>>>>>>>>>" + requestURI);

        // 获取session， 判断session用户信息是否存在
        Object user = request.getSession().getAttribute("userInfo");
        log.info("当前登录用户：{}", JSONObject.toJSONString(user));
        // @todo 正式使用的时候要打开
//        if(user == null){
//            // 未登录
//            log.debug("未登录请求：" + request.getRequestURI());
//            response.sendRedirect(request.getContextPath() + "/login");
//            return false;
//        }
        log.debug("放行请求：" + request.getRequestURI());
        return true;
    }
}
