///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.mussum.controllers.security;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//
///**
// *
// * @author yurin
// */
//@Configuration
//public class CustomAdapter extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//	http.addFilterAfter(
//		new AllowFilter(), BasicAuthenticationFilter.class);
//	http
//		.authorizeRequests()
//		.requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
//		.antMatchers("/**").hasRole("USER")
//		.and()
//		.httpBasic();
//    }
//}
