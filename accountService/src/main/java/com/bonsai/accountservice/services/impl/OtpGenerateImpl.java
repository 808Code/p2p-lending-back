package com.bonsai.accountservice.services.impl;

import com.bonsai.accountservice.services.OtpGenerateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Random;


@Service
public class OtpGenerateImpl implements OtpGenerateService {

    @Override
    public String generateOTP() {
        String otp= new DecimalFormat("0000").format(new Random().nextInt(9999));
        System.out.println(otp);
        return otp ;
    }
}
