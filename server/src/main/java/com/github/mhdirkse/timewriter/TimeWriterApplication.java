package com.github.mhdirkse.timewriter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;

@SpringBootApplication
public class TimeWriterApplication {
	public static void main(String[] args) {
		SpringApplication.run(TimeWriterApplication.class, args);
	}
}
