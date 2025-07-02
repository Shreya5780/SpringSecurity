package com.example.security.springsecurity.controller;

import com.example.security.springsecurity.model.UserModel;
import com.example.security.springsecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserModel registerUser(@RequestBody UserModel userModel) {
        return userService.registerUser(userModel);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserModel userModel) {
        return userService.verify(userModel);
    }
}
