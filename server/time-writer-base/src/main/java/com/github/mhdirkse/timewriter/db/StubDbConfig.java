package com.github.mhdirkse.timewriter.db;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}