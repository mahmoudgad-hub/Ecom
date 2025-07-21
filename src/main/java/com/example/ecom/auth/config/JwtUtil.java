package com.example.ecom.auth.config;


import com.example.ecom.auth.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private final JwtConfig jwtConfig;
    private Key key;

    public JwtUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getEncoder().encode(jwtConfig.getSecret().getBytes());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public String generateToken(String username, Long userId, String fullname) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userid", userId);
        claims.put("fullname", fullname);

        return Jwts.builder()
                .addClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getAccessExpiration()))
                .signWith(key)
                .compact();
    }
//    public String generateToken(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
//                .signWith(key)
//                .compact();
//    }

    public String generateRefreshToken(CustomUserDetails customUserDetails) {
        return createToken(new HashMap<>(), customUserDetails.getUsername(), customUserDetails.getUserId(), customUserDetails.getFullName());
    }

    private String createToken(Map<String, Object> claims, String subject, Long userId, String fullname) {
        Map<String, Object> claimsRef = new HashMap<>();
        claims.put("userid", userId);
        claims.put("fullname", fullname);

        return Jwts.builder()
                .addClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getRefreshExpiration() * 1000L))
                // .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
                .signWith(key)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long extractUserId(String token) {
        return extractAllClaims(token).get("userid", Long.class);
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean validateToken(String token, String username) {

        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

//    public boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }

    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getExpiration();
        return expiration.before(new Date());
    }

    /// ////////////
    public Date extractExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }


}


