package com.bonsai.accountservice.services.impl;

import com.bonsai.accountservice.constants.FileCategory;
import com.bonsai.accountservice.dto.request.UserCredentialDto;
import com.bonsai.accountservice.dto.request.RegisterKYCRequest;
import com.bonsai.accountservice.dto.request.VerifyOTPRequest;
import com.bonsai.accountservice.dto.storage.OTP;
import com.bonsai.accountservice.models.*;
import com.bonsai.accountservice.repositories.KYCRepo;
import com.bonsai.accountservice.models.UserCredential;
import com.bonsai.accountservice.repositories.UserCredentialRepo;
import com.bonsai.accountservice.services.EmailService;
import com.bonsai.accountservice.services.OTPService;
import com.bonsai.accountservice.services.OTPStorage;
import com.bonsai.accountservice.services.RegistrationService;
import com.bonsai.sharedservice.exceptions.AppException;
import com.bonsai.sharedservice.exceptions.InvalidOTPException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import java.time.LocalDate;
import com.bonsai.accountservice.services.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
@Slf4j
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final EmailService emailService;
    private final OTPService otpService;
    private final OTPStorage otpStorage;
    private final UserCredentialRepo userCredentialRepo;
    private final KYCRepo kycRepo;
    private final StorageService storageService;
    private final ObjectMapper objectMapper;
    private final StorageNameService storageName;
    private Contact contact;
    private Finance finance;
    private Address permanentAddress;
    private Address temporaryAddress;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void sendEmailOTP(String email) {
        String otp = otpService.generateOTP();

        String emailBody = "otpCode is "+otp;
        String subject = "Verify your Email";

        emailService.sendEmail(email, subject, emailBody);

        otpStorage.save(email, OTP.builder().otpCode(otp).verification(false).build());

    }

    @Override
    public void verifyEmailOTP(VerifyOTPRequest request) {
        String email=request.email();
        OTP otpCodeWithVerification = otpStorage.getOtp(email);

        if(otpCodeWithVerification == null){
            throw new InvalidOTPException("Receive an OTP code first.");
        }
        String storedOtp = otpCodeWithVerification.getOtpCode();
        if(storedOtp == null || !storedOtp.equals(request.otp())){
            throw new InvalidOTPException("otpCode not verified");
        }
        otpCodeWithVerification.setVerification(true);
        log.info("Changed email {} {}",email,otpCodeWithVerification );

    }

    @Override
    public void saveEmailPassword(UserCredentialDto request, String role) {
        String email = request.email();

        if(userCredentialRepo.findByEmail(email).isPresent()){
            throw new InvalidOTPException("Email Already Registered.");
        }
        OTP otpCodeWithVerification = otpStorage.getOtp(email);
        if (otpCodeWithVerification == null || !otpCodeWithVerification.getVerification()) {
            throw new InvalidOTPException("otpCode not verified");
        }
        userCredentialRepo.save(
                UserCredential.builder()
                        .email(request.email())
                        .password(bCryptPasswordEncoder.encode(request.password()))
                        .role(role)
                        .build()
        );
        otpStorage.delete(email);
    }

    @Transactional
    @Override
    public void saveKYC(RegisterKYCRequest request) {
        MultipartFile profilePhotoFile=request.profilePhoto();
        MultipartFile citizenShipPhotoFrontFile=request.citizenShipPhotoFront();
        MultipartFile citizenShipPhotoBackFile=request.citizenShipPhotoBack();

        String profilePhotoFileStorageName=storageName.storageNameGenerator(profilePhotoFile.getOriginalFilename(), FileCategory.PROFILE_PHOTO);
        String citizenShipPhotoFrontFileStorageName=storageName.storageNameGenerator(citizenShipPhotoFrontFile.getOriginalFilename(),FileCategory.CITIZENSHIP_FRONT);
        String citizenShipPhotoBackFileStorageName=storageName.storageNameGenerator(citizenShipPhotoBackFile.getOriginalFilename(),FileCategory.CITIZENSHIP_BACK);
        try {
            contact = objectMapper.readValue(request.contact(),Contact.class);
            finance = objectMapper.readValue(request.finance(),Finance.class);
            permanentAddress = objectMapper.readValue(request.permanentAddress(),Address.class);
            temporaryAddress = objectMapper.readValue(request.temporaryAddress(),Address.class);
        } catch (JsonProcessingException e) {
            throw new AppException("Unable to process Json Data", HttpStatus.BAD_REQUEST);
        }
        KYC kyc = KYC.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .middleName(request.middleName())
                .citizenShipNumber(request.citizenShipNumber())
                .dob(LocalDate.parse(request.dob()))
                .maritalStatus(request.maritalStatus())
                .children(Boolean.parseBoolean(request.children()))
                .currentlyStudying(Boolean.parseBoolean(request.currentlyStudying()))
                .occupation(request.occupation())
                .finance(finance)
                .contact(contact)
                .permanentAddress(permanentAddress.toString())
                .temporaryAddress(temporaryAddress.toString())
                .profilePhoto(profilePhotoFileStorageName)
                .citizenShipPhotoFront(citizenShipPhotoFrontFileStorageName)
                .citizenShipPhotoBack(citizenShipPhotoBackFileStorageName)
                .build();

        kycRepo.save(kyc);
        UserCredential userCredential = userCredentialRepo.findByEmail(request.email())
                .orElseThrow(() ->new AppException("Email not found in database.",HttpStatus.NOT_FOUND));
        userCredential.setKyc(kyc);
        userCredentialRepo.save(userCredential);
        storageService.store(profilePhotoFile,profilePhotoFileStorageName);
        storageService.store(citizenShipPhotoFrontFile,citizenShipPhotoFrontFileStorageName);
        storageService.store(citizenShipPhotoBackFile,citizenShipPhotoBackFileStorageName);

    }
}
