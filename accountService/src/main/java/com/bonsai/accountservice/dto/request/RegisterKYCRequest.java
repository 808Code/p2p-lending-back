package com.bonsai.accountservice.dto.request;


import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public record RegisterKYCRequest(
        String email,
        String firstName,
        String middleName,
        String lastName,
        String gender,
        String citizenShipNumber,
        String dob,
        String maritalStatus,
        String children,
        String currentlyStudying,
        String occupation,
        String temporaryAddress,
        String permanentAddress,
        String contact,
        String finance,
        @NotNull
        MultipartFile profilePhoto,
        @NotNull
        MultipartFile citizenShipPhotoFront,
        @NotNull
        MultipartFile citizenShipPhotoBack
        ){}

