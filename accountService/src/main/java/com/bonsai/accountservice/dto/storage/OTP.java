package com.bonsai.accountservice.dto.storage;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
public class OTP{
    String otpCode;
    Boolean verification;

}
