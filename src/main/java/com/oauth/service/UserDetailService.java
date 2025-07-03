package com.oauth.service;

import com.oauth.model.UserModel;
import com.oauth.model.UserPrincipal;
import com.oauth.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserModel user = userRepo.findByUsername(username);
        if(user == null){

            throw new UsernameNotFoundException("Username not found");
        }

        return new UserPrincipal(user);
    }
}
