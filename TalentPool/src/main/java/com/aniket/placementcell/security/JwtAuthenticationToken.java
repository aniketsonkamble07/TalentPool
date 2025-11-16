package com.aniket.placementcell.security;


import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String token;        // The raw JWT token
    private Object principal;          // The authenticated user (if known)

    // Constructor for unauthenticated token (before validation)
    public JwtAuthenticationToken(String token) {
        super(null);
        this.token = token;
        this.setAuthenticated(false);
    }

    // Constructor for authenticated token (after validation)
    public JwtAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.token = null;
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token; // the JWT token acts as credentials
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}