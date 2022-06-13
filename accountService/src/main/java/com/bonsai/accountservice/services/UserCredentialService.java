package com.bonsai.accountservice.services;

import com.bonsai.accountservice.dto.request.UserCredentialDto;
import com.bonsai.sharedservice.exceptions.AppException;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-06-11
 */
public interface UserCredentialService {
    UserCredentialDto findByEmail(String email) throws AppException;
}
