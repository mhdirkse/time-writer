package com.github.mhdirkse.timewriter.h2;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public interface SecurityRefinerForH2 {
    public void refine(HttpSecurity http) throws Exception;
}
