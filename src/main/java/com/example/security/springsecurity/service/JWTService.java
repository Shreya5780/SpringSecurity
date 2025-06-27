package com.example.security.springsecurity.service;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.crypto.keygen.KeyGenerators.*;

@Service
public class JWTService {



    public String generateToken(String username) {

        Map<String,Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 2)) //for 2 min = 1000 milisecond(1 sec) * 60 second(1 min) *  2 min(2min)
                .and()
                .signWith(getKey())
                .compact();
    }

    private String secreteKey = "";

    public JWTService() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        SecretKey sk = keyGen.generateKey();
        secreteKey = Base64.getEncoder().encodeToString(sk.getEncoded());
    }

    private Key getKey() {
        byte[] decoder = Base64.getDecoder().decode(secreteKey.getBytes()); //dont pass direct string so divide into bytes and then return
        return Keys.hmacShaKeyFor(decoder);
    }
}
