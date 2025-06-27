package com.example.security.springsecurity.service;

import com.example.security.springsecurity.model.UserModel;
import com.example.security.springsecurity.repo.USerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {

    @Autowired
    private USerRepo userRepo;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public UserModel registerUser(UserModel userModel) {

        userModel.setPassword(bCryptPasswordEncoder.encode(userModel.getPassword()));

        return userRepo.save(userModel);
    }

    public String verify(UserModel userModel) {
        Authentication auth = authenticationManager
                                              .authenticate(new UsernamePasswordAuthenticationToken(userModel.getUsername(), userModel.getPassword()));

        if(auth.isAuthenticated()) {
            return jwtService.generateToken(userModel.getUsername());
        }
        return "fail";
    }
}
