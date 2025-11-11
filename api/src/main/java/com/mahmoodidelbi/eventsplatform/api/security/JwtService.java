package com.mahmoodidelbi.eventsplatform.api.security;

import com.mahmoodidelbi.eventsplatform.api.config.JwtConfig;
import com.mahmoodidelbi.eventsplatform.api.entites.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class JwtService {
    private final JwtConfig jwtConfig;

    // Generate access tokens for users [short life]
    public Jwt generateAccessToken(User user) {
        return generateToken(user, jwtConfig.getAccessTokenExpiration());
    }

    // Generate refresh token [long life]
    public Jwt generateRefreshToken(User user) {
        return generateToken(user, jwtConfig.getRefreshTokenExpiration());

    }

    // Generate a jwt token, builds a claim, and returns a jwt object
    private Jwt generateToken(User user, int expirationSeconds) {
        Claims claims = Jwts.claims() //
                .subject(user.getId().toString()) // Adding keys
                .add("email", user.getEmail())
                .add("username", user.getUsername())
                .add("role", user.getRole())
                //
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (1000L * expirationSeconds)))
                .build();
        return new Jwt(claims, jwtConfig.getSecretKey());

    }

    // Parse and validate jwt token string
    public Jwt parseToken(String token) {
        try {
            Claims claims = getClaims(token);
            return new Jwt(claims, jwtConfig.getSecretKey());
        } catch (JwtException e) {
            // If the token is expired or invalid
            return null;
        }
    }

    // Extract claims from token string
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
