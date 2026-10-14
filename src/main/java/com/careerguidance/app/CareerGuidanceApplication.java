package com.careerguidance.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class CareerGuidanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CareerGuidanceApplication.class, args);
    }
}
