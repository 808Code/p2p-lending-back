package com.bonsai.sharedservice.dtos.response;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-07-23
 */
public record DtoInvalidErrorResponse(
        String message,
        Object errors
) {}
