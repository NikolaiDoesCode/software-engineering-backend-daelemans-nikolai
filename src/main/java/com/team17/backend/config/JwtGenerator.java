package com.team17.backend.config;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.team17.backend.User.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtGenerator {

    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 8;

    private final String SECRET_KEY = "/PVRR7N1kWmr3AMAqog4tR3Kj0iBlbVYcez2uA8dAW/LFfgtYkWW+XYfH7zbFoRtoEotW2hVBml7UMc/FdhN/Q==";

    private SecretKey key;

    public JwtGenerator() {
        byte[] decodedKey = Base64.getDecoder()
                .decode(SECRET_KEY);
        this.key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA512");
    }

    public String generateToken(User user) {

        Claims claims = Jwts.claims();

        claims.put("email", user.getEmail());
        claims.put("roles", List.of("ROLE_" + user.getRole().toString()));

        String jwt = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        System.out.println(jwt);
        return jwt;
    }

    public Claims decodeToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        return decodeToken(token).get("email", String.class);
    }

    public Date getExpirationDateFromToken(String token) {
        return decodeToken(token).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    public boolean validateToken(String token, User user) {
        String email = getEmailFromToken(token);
        return email.equals(user.getEmail()) && !isTokenExpired(token);
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    public String refreshToken(String token) {
        Claims claims = decodeToken(token);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}