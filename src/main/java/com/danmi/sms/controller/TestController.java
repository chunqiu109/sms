package com.danmi.sms.controller;

import com.danmi.sms.common.vo.Result;
import com.danmi.sms.entity.request.UserRequest;
import com.danmi.sms.utils.SMSUtils;
import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("test")
    public void list() {
        ArrayList<String> strings = Lists.newArrayList("13051835071");
        SMSUtils.send(strings,"ceshi");
    }
}
