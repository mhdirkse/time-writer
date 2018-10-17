package com.github.mhdirkse.timewriter.db;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
class RealDbConfig {
    @Bean
    @Primary
    public SecurityRefinerForH2 securityRefinerForH2Noop() {
        return new SecurityRefinerForH2Noop();
    }
}