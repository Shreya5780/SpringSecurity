package com.example.security.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain springSecurity(HttpSecurity http) throws Exception {

//        disable csrf(close)
        http.csrf(c -> c.disable());

//        enale autherization
        // because we write http.build with @bean it disable the autherization, so we need to enalee
//        ?it show output as access to locahost is denied
        http.authorizeHttpRequests(r -> r.anyRequest().authenticated());

        return http.build();
    }
}
