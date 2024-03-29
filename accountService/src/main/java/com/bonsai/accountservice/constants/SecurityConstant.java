package com.bonsai.accountservice.constants;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-06-11
 */
public class SecurityConstant {
    public static final String SECRET = "karnivore69";
    public static final long EXPIRATION_TIME = 86_400_000L; // 24 hrs
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
