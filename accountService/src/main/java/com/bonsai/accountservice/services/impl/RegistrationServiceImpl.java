package com.bonsai.accountservice.services.impl;


import com.bonsai.accountservice.constants.FileCategory;
import com.bonsai.accountservice.dto.request.CreateUserRequest;
import com.bonsai.accountservice.dto.request.RegisterKYCRequest;
import com.bonsai.accountservice.dto.request.VerifyOTPRequest;
import com.bonsai.accountservice.dto.storage.OTP;
import com.bonsai.accountservice.exceptions.InvalidOTPException;
import com.bonsai.accountservice.models.*;
import com.bonsai.accountservice.repositories.KYCRepo;
import com.bonsai.accountservice.repositories.UserCredentialRepo;
import com.bonsai.accountservice.services.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;


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

        String storedOtp= otpCodeWithVerification.getOtpCode();
        if(storedOtp == null || !storedOtp.equals(request.otp())){
            throw new InvalidOTPException("otpCode not verified");
        }
        otpCodeWithVerification.setVerification(true);
        log.info("Changed email {} {}",email,otpCodeWithVerification );

    }

    @Override
    public void saveEmailPassword(CreateUserRequest request,String role) {
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
                        .password(request.password())
                        .role(role)
                        .build()
        );
        otpStorage.delete(email);



    }

    @Transactional
    @Override
    public void saveKYC(RegisterKYCRequest request) {
        log.info("Request = {}",request);


        MultipartFile profilePhotoFile=request.profilePhoto();
        MultipartFile citizenShipPhotoFrontFile=request.citizenShipPhotoFront();
        MultipartFile citizenShipPhotoBackFile=request.citizenShipPhotoBack();

        String profilePhotoFileStorageName=storageName.storageNameGenerator(profilePhotoFile.getOriginalFilename(), FileCategory.PROFILE_PHOTO);
        String citizenShipPhotoFrontFileStorageName=storageName.storageNameGenerator(citizenShipPhotoFrontFile.getOriginalFilename(),FileCategory.CITIZENSHIP_FRONT);
        String citizenShipPhotoBackFileStorageName=storageName.storageNameGenerator(citizenShipPhotoBackFile.getOriginalFilename(),FileCategory.CITIZENSHIP_BACK);

        //here needs better exception handling
        try {
            contact=objectMapper.readValue(request.contact(),Contact.class);
            finance=objectMapper.readValue(request.finance(),Finance.class);
            permanentAddress=objectMapper.readValue(request.permanentAddress(),Address.class);
            temporaryAddress=objectMapper.readValue(request.temporaryAddress(),Address.class);
        } catch (JsonProcessingException e) {
            //what app exception for and error code?
            //or new but string so can use an toher string
            e.printStackTrace();
        }




        KYC kyc=KYC.builder()
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

        Optional<UserCredential> optionalUserCredential=userCredentialRepo.findByEmail(request.email());
//        if(!optionalDepartment.isPresent()){
//            throw new ResourceNotFound
//        }
//
        UserCredential userCredential = optionalUserCredential.get();
        userCredential.setKyc(kyc);
        userCredentialRepo.save(userCredential);

//        finance.setKyc(kyc);
//        financeRepo.save(finance);

        storageService.store(profilePhotoFile,profilePhotoFileStorageName);
        storageService.store(citizenShipPhotoFrontFile,citizenShipPhotoFrontFileStorageName);
        storageService.store(citizenShipPhotoBackFile,citizenShipPhotoBackFileStorageName);




    }
}
