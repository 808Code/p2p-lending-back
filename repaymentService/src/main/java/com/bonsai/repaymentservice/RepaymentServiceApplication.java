package com.bonsai.repaymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.bonsai"})
@EntityScan(basePackages = {"com.bonsai"})
public class RepaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RepaymentServiceApplication.class, args);
    }

}
