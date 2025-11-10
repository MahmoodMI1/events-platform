package com.mahmoodidelbi.eventsplatform.api.config;

import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
@ConfigurationProperties(prefix = "spring.jwt")
@Data
public class JwtConfig {
    // This class load properties from yamal

    // Automatically maps to yamal, converts to camelCase
    private String secret;
    private int accessTokenExpiration;
    private int refreshTokenExpiration;

    public SecretKey getSecretKey() {
        // Converts the string into proper format
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
