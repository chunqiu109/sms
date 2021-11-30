package com.danmi.sms.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserCodeUtil {

    private static Integer roleDigit;

    @Value("${role.digit:5}")
    public void setRoleDigit(Integer roleDigit) {
        this.roleDigit = roleDigit;
    }

//
    public static String generateRegisterCode(){
        String userCode ="";

        for (int i = 0; i < roleDigit; i++) {
            if (i ==0  || i == roleDigit-1){
                userCode = userCode+"1";
            } else {
                userCode = userCode+"0";
            }
        }
        return userCode;
    }

    public static void main(String[] args) {
        String s = generateRegisterCode();
        System.out.println(s);
    }
}
