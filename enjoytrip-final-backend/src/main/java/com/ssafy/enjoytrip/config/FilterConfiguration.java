package com.ssafy.enjoytrip.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

import com.ssafy.enjoytrip.filter.AuthFilter;
import com.ssafy.enjoytrip.util.JWTUtil;

@Configuration
public class FilterConfiguration {
   private final JWTUtil jwtUtil;
   
   public FilterConfiguration(JWTUtil jwtUtil) {
       this.jwtUtil = jwtUtil;
   }

   @Bean
   public AntPathMatcher antPathMatcher() {
       return new AntPathMatcher();
   }
   
   @Bean
   public FilterRegistrationBean<AuthFilter> authFilter() {
       FilterRegistrationBean<AuthFilter> filter = new FilterRegistrationBean<>();
       filter.setFilter(new AuthFilter(jwtUtil, antPathMatcher()));
       filter.setOrder(1);
       return filter;
   }
}
