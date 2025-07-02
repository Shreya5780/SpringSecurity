package com.example.security.springsecurity.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {
    @Autowired
     ApplicationContext applicationContext;
    @Autowired
    private HttpServletResponse httpServletResponse;

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

    public JWTService(ApplicationContext applicationContext) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        SecretKey sk = keyGen.generateKey();
        secreteKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        System.out.println("secreteKey: " + secreteKey);
        this.applicationContext = applicationContext;
    }

    private Key getKey() {
        byte[] decoder = Base64.getDecoder().decode(secreteKey.getBytes()); //dont pass direct string so divide into bytes and then return
        return Keys.hmacShaKeyFor(decoder);
    }


    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extrackUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean verifyToken(String token, UserDetails user) {
        final String username = extrackUserName(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }


    public long expirationTimeLeft(String authToken) {
        Date timeLeft = extractExpiration(authToken);
        return timeLeft.getTime() - System.currentTimeMillis();
    }


    public String refreshToken(String oldToken, HttpServletRequest request) {
        System.out.println(oldToken+" refreshed tokennnnnnnnnnnnn");
        String username = extrackUserName(oldToken);

        String newToken= generateToken(username);
        System.out.println("newToken...................................."+newToken);
        return newToken;

    }

    public String AuthenticateToken(String authToken, HttpServletRequest request) {
        String username = extrackUserName(authToken);
        UserDetails user = (UserDetails) applicationContext.getBean(MyUserDetailService.class).loadUserByUsername(username);
        if(verifyToken(authToken, user)){
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            token.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(token);

            httpServletResponse.setHeader("Authorization", "Bearer " + authToken);
            System.out.println("header......."+ httpServletResponse.getHeader("Authorization"));

        }
        return httpServletResponse.getHeader("Authorization");
    }
}
