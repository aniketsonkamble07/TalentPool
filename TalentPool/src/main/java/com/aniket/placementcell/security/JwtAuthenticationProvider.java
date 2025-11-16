package com.aniket.placementcell.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationProvider(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        System.out.println("JwtAuthenticationProvider.authenticate() called");

        String token = (String) authentication.getCredentials();
        System.out.println("Token received: " + (token != null ? token.substring(0, Math.min(10, token.length())) + "..." : "null"));

        if (token == null || token.isBlank()) {
            System.out.println("Token is null or blank");
            throw new BadCredentialsException("JWT token is missing");
        }

        if (!jwtUtil.validateToken(token)) {
            System.out.println("Token validation failed");
            throw new BadCredentialsException("Invalid or expired JWT");
        }

        String username = jwtUtil.extractUsername(token);
        System.out.println("Extracted username from token: " + username);

        if (username == null || username.isBlank()) {
            System.out.println("Username is null or blank in token");
            throw new BadCredentialsException("Invalid JWT: No username found");
        }

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            System.out.println("User details loaded successfully for: " + username);
            System.out.println("User authorities: " + userDetails.getAuthorities());

            UsernamePasswordAuthenticationToken authResult = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    token,
                    userDetails.getAuthorities()
            );

            System.out.println("Authentication successful for user: " + username);
            return authResult;

        } catch (Exception e) {
            System.out.println("Error loading user details: " + e.getMessage());
            e.printStackTrace();
            throw new BadCredentialsException("User not found: " + username);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        boolean supports = UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
        System.out.println("JwtAuthenticationProvider supports " + authentication.getName() + ": " + supports);
        return supports;
    }
}