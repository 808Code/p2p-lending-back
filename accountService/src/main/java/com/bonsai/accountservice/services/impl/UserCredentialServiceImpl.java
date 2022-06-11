package com.bonsai.accountservice.services.impl;

import com.bonsai.accountservice.dto.request.UserCredentialDto;
import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.accountservice.repositories.UserCredentialRepo;
import com.bonsai.accountservice.services.UserCredentialService;
import com.bonsai.sharedservice.exceptions.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-06-11
 */
@Service
@RequiredArgsConstructor
public class UserCredentialServiceImpl implements UserCredentialService {

    private final UserCredentialRepo userCredentialRepo;

    @Override
    public UserCredentialDto findByEmailAndRole(String email, String role) throws AppException {
        UserCredential userCredential = userCredentialRepo.findByEmailAndRole(email, role)
                .orElseThrow(
                        () -> new AppException(role + " of this email doesn't exist", HttpStatus.BAD_REQUEST)
                );
        return new UserCredentialDto(userCredential);
    }
}
