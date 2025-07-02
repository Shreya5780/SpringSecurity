package com.example.security.springsecurity.config;

import com.example.security.springsecurity.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter /* one filter created for every request */ {

    @Autowired
    private JWTService  jwtService;

    @Autowired
     ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            username = jwtService.extrackUserName(token);
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            System.out.println("USer lost" + username);
            jwtService.AuthenticateToken(token, request);
        }

        filterChain.doFilter(request,response);
    }
}
