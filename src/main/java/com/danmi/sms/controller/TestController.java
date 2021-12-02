package com.danmi.sms.controller;

import com.danmi.sms.common.vo.Result;
import com.danmi.sms.entity.User;
import com.danmi.sms.entity.request.UserRequest;
import com.danmi.sms.utils.SMSUtils;
import com.danmi.sms.utils.UserUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {
    @Autowired
    private UserUtils userUtils;
    @GetMapping("test")
    public void list() {
        User user = userUtils.getUser();
        boolean systemAdmin = userUtils.isSystemAdmin();
        System.out.println(user);
        System.out.println(systemAdmin);
    }
}
