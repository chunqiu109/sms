//package com.danmi.sms.common.config;
//
//import com.alibaba.nacos.api.annotation.NacosInjected;
//import com.alibaba.nacos.api.exception.NacosException;
//import com.alibaba.nacos.api.naming.NamingService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//
//import javax.annotation.PostConstruct;
//
//@Configuration
//@Slf4j
//public class NacosConfig {
//    @Value("${server.port}")
//    private int serverPort;
//    @Value("${spring.application.name}")
//    private String applicationName;
//
//    @NacosInjected
//    private NamingService namingService;
//
//    @PostConstruct
//    public void registerInstance() throws NacosException {
//
//        namingService.registerInstance(applicationName, "127.0.0.1", serverPort);
//    }
//}
