package com.mahmoodidelbi.eventsplatform.api.security;

import com.mahmoodidelbi.eventsplatform.api.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;

import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    // Will Load our user from the data base

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Load user from data base
        var user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));

        // Convert to Spring Security UserDetail
        return new User( // UserDetail "USER"
                user.getEmail(),
                user.getPasswordHash(),
                List.of(new SimpleGrantedAuthority(("Role_" + user.getRole().name())))
        );
    }



}
