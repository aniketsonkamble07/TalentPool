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
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final List<String> PUBLIC_PATHS = List.of(
            "/auth/login", "/auth/register", "/auth/verify",
            "/student/register", "/css/", "/js/", "/images/", "/public/",
            "/error", "/favicon.ico", "/webjars/",
            "/v3/api-docs", "/swagger-ui/", "/swagger-resources/"
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

        System.out.println("\n Processing " + method + " " + path);

        try {
            String token = extractToken(request);

            if (token != null) {
                System.out.println("JWT token found, validating...");

                if (jwtUtil.validateToken(token)) {
                    setAuthenticationContext(token, request);
                    System.out.println("Authentication successful");
                } else {
                    System.out.println("Invalid JWT token");
                    sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                            "Invalid or expired token");
                    return;
                }
            } else {
                System.out.println("No JWT token found in request");
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "Authorization token required");
                return;
            }
        } catch (JwtException e) {
            System.out.println("JWT validation failed: " + e.getMessage());
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Token validation failed: " + e.getMessage());
            return;
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Internal server error");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7).trim();
            if (!token.isEmpty()) {
                System.out.println("Token extracted from Authorization header");
                System.out.println("FULL TOKEN RECEIVED: " + token);
                System.out.println("TOKEN LENGTH: " + token.length());
                System.out.println("TOKEN STARTS WITH: " + token.substring(0, Math.min(20, token.length())));
                return token;
            }
        }
        // 2. If header not present, check cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    System.out.println("Token extracted from cookie");
                    return token; // now we return the cookie token
                }
            }
        }


        System.out.println("Authorization Header: " + authHeader);
        return null;
    }

    private String getTokenPreview(String token) {
        if (token.length() <= 50) {
            return token;
        }
        return token.substring(0, 20) + "..." + token.substring(token.length() - 10);
    }

    private void setAuthenticationContext(String token, HttpServletRequest request) {
        Claims claims = jwtUtil.extractAllClaims(token);
        String username = claims.getSubject();
        String role = claims.get("role", String.class);

        System.out.println("Setting authentication for: " + username + " | Role: " + role);

        if (username != null && role != null) {
            var authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + role)
            );

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            System.out.println("Security context set for user: " + username);
        } else {
            throw new JwtException("Invalid token: missing username or role");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message)
            throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        boolean shouldSkip = PUBLIC_PATHS.stream().anyMatch(path::startsWith);
        System.out.println(path + " - Skip filtering: " + shouldSkip);
        return shouldSkip;
    }
}