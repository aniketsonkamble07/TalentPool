package com.aniket.placementcell.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final List<String> PUBLIC_PATHS = List.of(
            "/pvg/register", "/pvg/login", "/css/", "/js/", "/images/", "/public/",
            "/error", "/favicon.ico", "/webjars/", "/v3/api-docs", "/swagger-ui/"
    );

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        System.out.println("Processing request: " + method + " " + path);

        // Skip JWT validation for public endpoints
        if (isPublicPath(path)) {
            System.out.println("Skipping JWT validation for public path: " + path);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Optional<String> token = extractToken(request);

            if (token.isPresent()) {
                System.out.println("JWT token found, validating...");
                if (jwtUtil.validateToken(token.get())) {
                    setAuthenticationContext(token.get(), request);
                    System.out.println("Authentication set successfully");
                } else {
                    System.out.println("Invalid JWT token");
                }
            } else {
                System.out.println("No JWT token found in request");
            }
        } catch (JwtException e) {
            System.out.println("JWT validation failed: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Invalid or expired token\"}");
            return;
        } catch (Exception e) {
            System.out.println("Unexpected error during JWT authentication: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        boolean isPublic = PUBLIC_PATHS.stream().anyMatch(path::startsWith);
        System.out.println("Path: " + path + " is public: " + isPublic);
        return isPublic;
    }

    private Optional<String> extractToken(HttpServletRequest request) {
        // Try Authorization header first
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("Token extracted from Authorization header");
            return Optional.of(token);
        }

        // Try cookies
        if (request.getCookies() != null) {
            Optional<String> cookieToken = Arrays.stream(request.getCookies())
                    .filter(c -> "jwt".equals(c.getName()))
                    .map(Cookie::getValue)
                    .findFirst();

            if (cookieToken.isPresent()) {
                System.out.println("Token extracted from cookie");
            } else {
                System.out.println("No JWT cookie found");
            }
            return cookieToken;
        }

        System.out.println("No token found in request");
        return Optional.empty();
    }

    private void setAuthenticationContext(String token, HttpServletRequest request) {
        Claims claims = jwtUtil.extractAllClaims(token);
        String username = claims.getSubject();
        String role = claims.get("role", String.class);

        System.out.println("Setting authentication for user: " + username + " with role: " + role);

        if (username != null) {
            var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            System.out.println("Authentication context set successfully for user: " + username);
        } else {
            System.out.println("Username is null in token");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        boolean shouldNotFilter = isPublicPath(path);
        System.out.println("Should not filter path " + path + ": " + shouldNotFilter);
        return shouldNotFilter;
    }
}