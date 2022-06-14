package com.bonsai.sharedservice.dtos.response;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-06-11
 */
public record LoginResponse(
        String message,
        String token,
        String role
) {
}
