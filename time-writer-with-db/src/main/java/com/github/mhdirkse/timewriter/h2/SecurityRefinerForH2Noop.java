package com.github.mhdirkse.timewriter.h2;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import com.github.mhdirkse.timewriter.h2.SecurityRefinerForH2;

public class SecurityRefinerForH2Noop implements SecurityRefinerForH2 {
    @Override
    public void refine(HttpSecurity http) throws Exception {
        // TODO: Remove these lines. They are now here to
        // fix running within AppEngine. 
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }
}
