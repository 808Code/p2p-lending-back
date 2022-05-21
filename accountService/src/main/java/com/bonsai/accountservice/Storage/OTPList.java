package com.bonsai.accountservice.Storage;

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.Hashtable;
import java.util.Map;


@Component
public class OTPList {
    private Hashtable<String, String> otpList=new Hashtable<>();

    public void add(String email,String otp){
      otpList.put(email,otp);
    }

    public void remove(String email){
        otpList.remove(email);
    }
    public void printContent(){
        for (Map.Entry<String, String> entry : otpList.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println ("Key: " + key + " Value: " + value);
        }
    }

}
