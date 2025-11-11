package com.mahmoodidelbi.eventsplatform.api.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {// Filter will run once per request

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Extract Authorization header
        String authHeader = request.getHeader("Authorization");

        // 2. Checks if the header exists and starts with the "Bearer"
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Get the token, remove "bearer "
        String token = authHeader.substring(7);

        // 4. Validate and parse the token
        Jwt jwt = jwtService.parseToken(token);

        if (jwt == null || jwt.isExpired()) {
            filterChain.doFilter(request, response);
            return;
        }

        // 6. Create authentication object
        var authentication = new UsernamePasswordAuthenticationToken(
                jwt.getUserId(),// Who is the user (Principal)
                null, // Credential
                List.of(new SimpleGrantedAuthority("ROLE_"+ jwt.getRole()))// Authorities
        );

        // 7. Set request details
        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        // 8. Set authentication in Spring security context
        SecurityContextHolder.getContext().setAuthentication(authentication);// Spring Security will know who the user is


        // 9. continue filter chain
        filterChain.doFilter(request, response);

    }
}
