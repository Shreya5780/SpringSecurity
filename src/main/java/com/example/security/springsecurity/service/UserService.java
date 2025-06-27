package com.example.security.springsecurity.service;

import com.example.security.springsecurity.model.UserModel;
import com.example.security.springsecurity.repo.USerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {

    @Autowired
    private USerRepo userRepo;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public UserModel registerUser(UserModel userModel) {

        userModel.setPassword(bCryptPasswordEncoder.encode(userModel.getPassword()));

        return userRepo.save(userModel);
    }
}
