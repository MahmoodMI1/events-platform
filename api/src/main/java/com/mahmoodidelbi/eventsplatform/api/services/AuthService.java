package com.mahmoodidelbi.eventsplatform.api.services;

import com.mahmoodidelbi.eventsplatform.api.dtos.AuthResponse;
import com.mahmoodidelbi.eventsplatform.api.dtos.LoginRequest;
import com.mahmoodidelbi.eventsplatform.api.dtos.RegisterRequest;
import com.mahmoodidelbi.eventsplatform.api.entites.User;
import com.mahmoodidelbi.eventsplatform.api.entites.Wallet;
import com.mahmoodidelbi.eventsplatform.api.enums.UserRole;
import com.mahmoodidelbi.eventsplatform.api.repositories.UserRepository;
import com.mahmoodidelbi.eventsplatform.api.repositories.WalletRepository;
import com.mahmoodidelbi.eventsplatform.api.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@AllArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    // Registering a new user
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Check if email exist
        if (userRepository.existsByEmail(request.getEmail())) {
            // [TEMP] Add costume exceptions for email
            throw new RuntimeException("Email already exists");
        }

        // Check if username exists
        if (userRepository.existsByUsername(request.getUsername())) {
            // [TEMP] Add costume exceptions for username
            throw new RuntimeException("Username already exists");
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.USER);

        // save new user
        user = userRepository.save(user);

        // Create a wallet for new user
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalance(BigDecimal.ZERO);
        walletRepository.save(wallet);

        // Generate user's JWT token
        String token = jwtService.generateAccessToken(user).toString();

        // return Authentication response
        return new AuthResponse(token);

    }

    // Login existing user
    public AuthResponse login(LoginRequest request) {
        // Authenticate user credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Load user from db
        User user = userRepository.findByEmail(request.getEmail())
                // [TEMP] Add costume exceptions for email
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate Jwt token
        String token = jwtService.generateAccessToken(user).toString();

        // return response
        return new AuthResponse(token);
    }
}
