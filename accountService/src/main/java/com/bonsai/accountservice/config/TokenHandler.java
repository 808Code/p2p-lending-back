package com.bonsai.accountservice.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

import static com.bonsai.accountservice.constants.SecurityConstant.*;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-06-11
 */
public class TokenHandler {

    public static String generateToken(String email) {
        return JWT.create()
                .withSubject(email)
                //expiration time is 15 minutes
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
    }

    public static String getEmailFromToken(String token) {
        return JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX,""))
                .getSubject();
    }
}