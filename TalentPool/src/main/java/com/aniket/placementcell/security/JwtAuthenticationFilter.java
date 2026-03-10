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

    // ✅ Public paths that should NOT require JWT
    private static final List<String> PUBLIC_PATHS = List.of(

            "/",            // root
            "/home",        // home page

            "/auth/login",
            "/auth/register",
            "/auth/verify",

            "/student/register",

            "/css/",
            "/js/",
            "/images/",
            "/static/",
            "/public/",

            "/error",
            "/favicon.ico",
            "/webjars/",

            "/v3/api-docs",
            "/swagger-ui/",
            "/swagger-resources/"
    );

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        System.out.println("\nProcessing " + method + " " + path);

        try {

            String token = extractToken(request);

            // ✅ If token exists → validate
            if (token != null) {

                System.out.println("JWT token found");

                if (jwtUtil.validateToken(token)) {

                    setAuthenticationContext(token, request);

                } else {

                    sendErrorResponse(
                            response,
                            HttpServletResponse.SC_UNAUTHORIZED,
                            "Invalid or expired token"
                    );

                    return;
                }

            }
            // ✅ If token not present → allow request (SecurityConfig will decide)
            else {

                System.out.println("No token found, continuing...");
            }

        } catch (JwtException e) {

            sendErrorResponse(
                    response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Token validation failed"
            );

            return;

        } catch (Exception e) {

            e.printStackTrace();

            sendErrorResponse(
                    response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Internal server error"
            );

            return;
        }

        filterChain.doFilter(request, response);
    }


    // ✅ Extract token from header or cookie
    private String extractToken(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            return authHeader.substring(7);
        }

        if (request.getCookies() != null) {

            for (Cookie cookie : request.getCookies()) {

                if ("jwt".equals(cookie.getName())) {

                    return cookie.getValue();
                }
            }
        }

        return null;
    }


    // ✅ Set authentication
    private void setAuthenticationContext(
            String token,
            HttpServletRequest request
    ) {

        Claims claims = jwtUtil.extractAllClaims(token);

        String username = claims.getSubject();

        String role = claims.get("role", String.class);

        if (username != null && role != null) {

            var authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + role)
            );

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            authorities
                    );

            authToken.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authToken);
        }
    }


    // ✅ Send JSON error
    private void sendErrorResponse(
            HttpServletResponse response,
            int status,
            String message
    ) throws IOException {

        response.setStatus(status);

        response.setContentType("application/json");

        response.getWriter().write(
                "{\"error\": \"" + message + "\"}"
        );
    }


    // ✅ Skip filter for public paths
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getRequestURI();

        boolean skip = PUBLIC_PATHS
                .stream()
                .anyMatch(path::startsWith);

        System.out.println(path + " -> Skip JWT: " + skip);

        return skip;
    }
}