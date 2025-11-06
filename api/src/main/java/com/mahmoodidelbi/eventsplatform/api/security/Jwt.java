package com.mahmoodidelbi.eventsplatform.api.security;

import com.mahmoodidelbi.eventsplatform.api.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;

public class Jwt {
    // This is a utility class, makes Jwt easier to work with

    private final Claims claims; // JWT Claims: the data inside the token
    private final SecretKey secretKey;

    // Constructor
    public Jwt(Claims claims,  SecretKey secretKey) {
        this.claims = claims;
        this.secretKey = secretKey;
    }

    public boolean isExpired() {
        // Compares the expiration date to the current date
        return claims.getExpiration().before(new Date());
    }

    public long getUserId() {
        // Extract from "subject" claim: standard jwt field
        return Long.valueOf(claims.getSubject());
    }

    public UserRole getRole() {
        // Converts the 'role' in claim to enum to use
        return UserRole.valueOf(claims.get("role", String.class));
    }

    public String getEmail() {

        return claims.get("email", String.class);
    }

    public String getUsername() {
        return claims.get("username", String.class);
    }

    @Override
    public String toString() {
        // converts the jwt object into a token string
        return Jwts.builder()
                .claims(claims)
                .signWith(secretKey)
                .compact();
    }
}
