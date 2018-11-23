package com.github.mhdirkse.timewriter.h2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
class StubDbConfig {
    @Bean
    public SecurityRefinerForH2 securityRefinerForH2() {
        return new SecurityRefinerForH2Impl();
    }

    @Bean
    public DataInitializerForH2 dataInitializerForH2() {
        return new DataInitializerForH2Impl();
    }

    @Bean
    public PasswordEncoder passwordEncoderForH2() {
        return NoOpPasswordEncoder.getInstance();
    }
}