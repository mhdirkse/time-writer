package com.github.mhdirkse.timewriter.db;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public class SecurityRefinerForH2Impl implements SecurityRefinerForH2 {
    @Override
    public void refine(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }
}
