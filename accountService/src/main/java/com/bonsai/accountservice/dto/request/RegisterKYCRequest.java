package com.bonsai.accountservice.dto.request;


import org.springframework.web.multipart.MultipartFile;

public record RegisterKYCRequest(
        String email,
        String firstName,
        String middleName,
        String lastName,
        String Gender,
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
        MultipartFile profilePhoto,
        MultipartFile citizenShipPhotoFront,
        MultipartFile citizenShipPhotoBack
        ){}

