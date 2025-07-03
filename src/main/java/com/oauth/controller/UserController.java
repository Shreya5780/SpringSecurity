package com.oauth.controller;

import com.oauth.model.LoginModel;
import com.oauth.model.UserModel;
import com.oauth.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserModel> register(@RequestBody @Valid UserModel user) {
//
        return userService.register(user);

    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginModel user) {
        if(user.getEmailOrUsername() == null || user.getPassword() == null){
            return new ResponseEntity<>("Please enter username or email and password", HttpStatus.BAD_REQUEST);
        }
        System.out.println(userService.login(user));
        return userService.login(user);
    }
}
