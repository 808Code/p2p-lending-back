package com.bonsai.accountservice.services.impl;

import com.bonsai.accountservice.services.OTPService;
import org.springframework.stereotype.Service;
import java.text.DecimalFormat;
import java.util.Random;


@Service
public class OTPServiceImpl implements OTPService {

    @Override
    public String generateOTP() {
        return new DecimalFormat("0000").format(new Random().nextInt(9999));
    }

}
