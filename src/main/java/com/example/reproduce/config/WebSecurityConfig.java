package com.example.reproduce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

/**
 * Simple http security configuration for demo project.
 * All endpoints are permitted for anyone except "/api/v1/item/**" in combination with PUT method.
 */
@Configuration
public class WebSecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User
                .withUsername("admin")
                .password(passwordEncoder.encode("secret"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setMatchingRequestParameterName(null);
        http.csrf().disable()
                .requestCache((cache) -> cache.requestCache(requestCache))
                .authorizeHttpRequests((requests) -> requests
                        // Only PUT request requires authorisation
                        .requestMatchers(HttpMethod.PUT, "/api/v1/item/**").hasRole("ADMIN")
                        .anyRequest().permitAll())
                .httpBasic();
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}