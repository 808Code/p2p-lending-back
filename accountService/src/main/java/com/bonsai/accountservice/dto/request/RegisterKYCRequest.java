package com.bonsai.accountservice.dto.request;


import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public record RegisterKYCRequest(
        UUID id,
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
        MultipartFile profilePhoto,
        MultipartFile citizenShipPhotoFront,
        MultipartFile citizenShipPhotoBack
        ){}

