package com.bonsai.accountservice.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bonsai.sharedservice.exceptions.AppException;
import org.springframework.http.HttpStatus;

import java.util.Date;

import static com.bonsai.accountservice.constants.SecurityConstant.*;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-06-11
 */
public class TokenHandler {

    public static String generateToken(String email, String role) {
        return JWT.create()
                .withSubject(email)
                .withClaim("role", role)
                //expiration time is 15 minutes
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
    }

    public static String getEmailFromToken(String token) {

        JWTVerifier verifier = JWT
                .require(Algorithm.HMAC512(SECRET.getBytes()))
                .build();

        return verifier
                .verify(token)
                .getSubject();
    }

    public static boolean hasTokenExpired(String token) {
        try {
            DecodedJWT decoder = JWT.decode(token);
            Date expiryDate = decoder.getExpiresAt();
            return expiryDate.before(new Date());
        } catch (Exception e) {
            throw new AppException("Invalid token", HttpStatus.BAD_REQUEST);
        }
    }


}