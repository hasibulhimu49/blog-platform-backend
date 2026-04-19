package com.example.blog_platform_api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;


@Service
@Slf4j
public class JwtService {

    //private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    //এই method নিজেই একটা random, secure, 256-bit secret key generate করে দেয়। তাই YAML বা .env-এ আলাদা করে key রাখার প্রয়োজন নেই

    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    public String generateToken(UserDetails userDetails) {
        //log.info("Generating token for username: {}", userDetails.getUsername());
        UserPrincipal userPrincipal =(UserPrincipal) userDetails;
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", userPrincipal.getUser().getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }



    public boolean isTokenValid(String token,UserDetails userDetails)
    {
        String username=extractUsername(token);
        return (username.equals(userDetails.getUsername())&&!isTokenExpired(token));
    }



    private boolean isTokenExpired(String token)
    {
        return extractAllClaims(token).getExpiration().before(new Date());
    }



    public String extractUsername(String token) {
        log.info("Extracting username from token: {}", token);
        return extractAllClaims(token).getSubject();
    }



    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build()
                .parseClaimsJws(token).getBody();
    }



    private Key getSignKey() {
        log.info("JWT Secret Key length: {}", SECRET_KEY.length());
       return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        //Best
       // byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);//byte[] keyBytes =SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        //return Keys.hmacShaKeyFor(keyBytes);
    }
}
