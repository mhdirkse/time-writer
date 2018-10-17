package com.github.mhdirkse.timewriter.db;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public class SecurityRefinerForH2Noop implements SecurityRefinerForH2 {
    @Override
    public void refine(HttpSecurity http) throws Exception {
    }
}
