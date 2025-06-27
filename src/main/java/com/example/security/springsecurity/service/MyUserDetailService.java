package com.example.security.springsecurity.service;

import com.example.security.springsecurity.model.UserModel;
import com.example.security.springsecurity.model.UserPrinciple;
import com.example.security.springsecurity.repo.USerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.util.ArrayList;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private USerRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserModel user = userRepo.findByUsername(username);
        if(user == null){

            throw new UsernameNotFoundException("Username not found");
        }

//        return new org.springframework.security.core.userdetails.User(
//                user.getUsername(),
//                user.getPassword(),
//                new ArrayList<>() // authorities/roles
//        );

        return new UserPrinciple(user);
    }
}
