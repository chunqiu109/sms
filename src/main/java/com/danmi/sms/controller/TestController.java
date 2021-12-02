package com.danmi.sms.controller;

import com.danmi.sms.entity.User;
import com.danmi.sms.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
