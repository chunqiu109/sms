package com.danmi.sms.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.danmi.sms.entity.User;

import java.util.Date;
import java.util.List;

public class TokenUtils {

    // 一小时过期
    private static final long EXPIRE_TIME = 60 * 60 * 1000;

    public static String getToken(User user) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);

        String token= JWT.create().withAudience(String.valueOf(user.getId()), String.valueOf(user.getRoleId())) // 将 user id 保存到 token 里面
                .withClaim("roleId", user.getRoleId())
                .withClaim("username", user.getUsername())
                .withClaim("id", user.getId())
                .withExpiresAt(date) //一小时后token过期
                .sign(Algorithm.HMAC256(user.getPassword())); // 以 password 作为 token 的密钥
        return token;
    }

    public static void main(String[] args) {
        User user = new User();
        user.setId(1).setPassword("1111").setRoleId(1);
        String token = getToken(user);
        System.out.println(token);
        DecodedJWT decode = JWT.decode(token);
        Date expiresAt = decode.getExpiresAt();
        System.out.println(expiresAt);

        String signature = JWT.decode(token).getSignature();
        List<String> audience = JWT.decode(token).getAudience();

    }
}
