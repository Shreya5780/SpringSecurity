package com.example.security.springsecurity.controller;

import com.example.security.springsecurity.model.StudentModel;
import com.example.security.springsecurity.service.JWTService;
import com.example.security.springsecurity.service.MyUserDetailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentControlle {

    @Autowired
            private MyUserDetailService myUserDetailService;

    List<StudentModel> students = new ArrayList<>(List.of(
            new StudentModel(1, "shreya", 33),
            new StudentModel(2, "het", 55)
    )
    );
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTService jWTService;

    @Autowired
    ApplicationContext context;


    @GetMapping("/student")
    public List<StudentModel> hello() {
        return students;
    }

    @GetMapping("/csrf")
    public CsrfToken csrf(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @PostMapping("/student")
    public String addStudent(@RequestBody StudentModel student, HttpServletRequest request, HttpServletResponse httpServletResponse) {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
        username = jWTService.extrackUserName(token);
            System.out.println(username + " " + token);
        }
        String newToken = jWTService.refreshToken(token, request);
        jWTService.AuthenticateToken(newToken, request);
        students.add(student);

        return newToken;

    }

//    @PostMapping("/user")
//    public UserDetails findAll() {
//       return   myUserDetailService.loadUserByUsername("shreya");
//    }
}
