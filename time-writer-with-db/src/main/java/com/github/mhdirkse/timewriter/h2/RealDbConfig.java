package com.github.mhdirkse.timewriter.h2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
class RealDbConfig {
    @Bean
    @Primary
    public SecurityRefinerForH2 securityRefinerForH2Noop() {
        return new SecurityRefinerForH2Noop();
    }

    @Bean
    @Primary
    public DataInitializerForH2 dataInitializerForH2Noop() {
        return new DataInitializerForH2Noop();
    }

    @Bean
    @Primary
    public PasswordEncoder passwordEncoderReal() {
        return new BCryptPasswordEncoder();
    }
}