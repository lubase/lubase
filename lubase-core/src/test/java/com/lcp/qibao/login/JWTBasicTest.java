package com.lcp.qibao.login;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lubase.core.service.impl.UserInfoServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class JWTBasicTest {

    @SneakyThrows
    @Test
    void testCreate() {
        String token = "";
        LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(2);
        token = JWT.create().withAudience("zhangsan")
                .withExpiresAt(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .withClaim("lisi", "lisivalue").
                sign(Algorithm.HMAC256("123"));
        System.out.println(token);
        System.out.println(ZoneId.systemDefault());

        DecodedJWT decodedJWT = JWT.decode(token);
        System.out.println("" + decodedJWT.getHeader());
        System.out.println("" + decodedJWT.getToken());
        System.out.println("" + decodedJWT.getPayload());
        System.out.println("" + decodedJWT.getSignature());
        System.out.println(decodedJWT.getExpiresAt());

        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("123")).build();
        boolean verifyResult = true;
        try {
            jwtVerifier.verify(token);
            System.out.println("ok");
        } catch (JWTVerificationException e) {
            throw new RuntimeException("401");
        }
        assert verifyResult;

        //token 超出有效期 所以结果是失败的
        Thread.sleep(3000);
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            //throw new RuntimeException("401");
            verifyResult = false;
        }
        assert !verifyResult;
    }

    @Test
    void testSign() {

        Algorithm obj = Algorithm.HMAC256("123123123");
        Algorithm obj2 = Algorithm.HMAC256("123123");
        String tmp = UserInfoServiceImpl.secretKey;

        System.out.println(tmp);
        String s = "";
    }
}
