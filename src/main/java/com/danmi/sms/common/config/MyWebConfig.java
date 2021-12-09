package com.danmi.sms.common.config;

import com.danmi.sms.utils.FilePathUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@Configuration
@Slf4j
public class MyWebConfig implements WebMvcConfigurer {

    @Autowired
    @Qualifier(value = "loginInterceptor")
    private HandlerInterceptor handlerInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration
                   = registry.addInterceptor(handlerInterceptor);
        // 拦截请求
        registration.addPathPatterns("/**");
        // 放行请求
        registration.excludePathPatterns(
                "/login",
                "/captcha",
                "/user/login",
                "/user/logout",
                "/layui/**",
                "/lib/**",
                "/webjars/**",
                "/api/**",
                "/css/**",
                "/js/**",
                "/images/**",
                "/test",
                "/error",
                "/mobile/**",
                "/upload/**",
                "/"
        );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String filePath = null;
        try {
            filePath = FilePathUtils.getFilePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info(" uploadPath >>>>>>>>>>>>>>>>>>>>" + filePath);
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + filePath + "/");
    }

}
