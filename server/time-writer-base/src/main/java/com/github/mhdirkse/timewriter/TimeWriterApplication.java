package com.github.mhdirkse.timewriter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@SpringBootApplication
public class TimeWriterApplication {
	public static void main(String[] args) {
		SpringApplication.run(TimeWriterApplication.class, args);
	}

	@Bean
	UserDetailsServiceImpl userDetailsService() {
	    return new UserDetailsServiceImpl();
	}

	@Bean
	AuthenticationFailureHandler authenticationFailureHandler() {
	    return new SimpleUrlAuthenticationFailureHandler();
	}
}
