package com.oauth.service;

import com.oauth.exception.UserAlreadyExistException;
import com.oauth.model.LoginModel;
import com.oauth.model.UserModel;
import com.oauth.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jWTService;

    public ResponseEntity<UserModel> register(UserModel user) {

        if(userRepo.existsByEmail(user.getEmail())){
            throw new UserAlreadyExistException(user.getEmail() + " is already exist");
        }
        if(userRepo.existsByUsername(user.getUsername())){
            throw new UserAlreadyExistException(user.getUsername()  + " is already exist"   );
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public ResponseEntity<String> login(LoginModel user) {
        String emailOrPassword = user.getEmailOrUsername();

        //check if email or username exist or not
        UserModel userModel = userRepo.findByEmail(emailOrPassword);
        if(userModel == null){
            userModel = userRepo.findByUsername(emailOrPassword);
        }
//        System.out.println(userModel.getEmail() + " " + userModel.getUsername()+   "  " +   userModel.getPassword()    + "user........");
        if (userModel == null) {
            throw new UsernameNotFoundException("User not found");
//            return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userModel.getUsername(), user.getPassword()));

//        if(authentication.isAuthenticated()) {
            String token = jWTService.generateToken(userModel.getUsername());
            return new ResponseEntity<>(token, HttpStatus.OK);
//        }

//        return new ResponseEntity<>("Authentication failed", HttpStatus.UNAUTHORIZED);

    }
}
