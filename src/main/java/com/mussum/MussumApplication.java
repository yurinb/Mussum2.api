package com.mussum;

import com.mussum.controllers.security.AllowFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MussumApplication {
    
    @Bean
    public FilterRegistrationBean getFiltroJWT() {
	FilterRegistrationBean frb = new FilterRegistrationBean();

	frb.setFilter(new AllowFilter());
	frb.addUrlPatterns("/admin/*");

	return frb;
    }

    public static void main(String[] args) {
	SpringApplication.run(MussumApplication.class, args);
    }
}
