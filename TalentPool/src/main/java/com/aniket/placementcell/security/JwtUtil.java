package com.aniket.placementcell.security;

import com.aniket.placementcell.enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    private static final long EXPIRATION_TIME = 86400000; // 24 hours

    private Key getSigningKey() {
        if (secretKey == null || secretKey.trim().isEmpty()) {
            throw new IllegalStateException("JWT secret key is not configured");
        }
        if (secretKey.length() < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 32 characters long");
        }
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username, Role role) {
        System.out.println("Generating JWT for user: " + username + " with role: " + role);

        String token = Jwts.builder()
                .setSubject(username)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        System.out.println("JWT generated successfully");
        return token;
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Role extractRole(String token) {
        String roleStr = extractAllClaims(token).get("role", String.class);
        return Role.valueOf(roleStr);
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired: " + e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            System.out.println("Malformed token: " + e.getMessage());
            throw e;
        } catch (SignatureException e) {
            System.out.println("Invalid signature: " + e.getMessage());
            throw e;
        } catch (JwtException e) {
            System.out.println(" JWT error: " + e.getMessage());
            throw e;
        }
    }

    public boolean validateToken(String token) {
        try {
            System.out.println(" Validating token...");

            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);

            System.out.println("Token is valid");
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired: " + e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            System.out.println("Malformed token: " + e.getMessage());
            return false;
        } catch (SignatureException e) {
            System.out.println("Signature mismatch: " + e.getMessage());
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }

    public boolean validateToken(String token, String username) {
        return validateToken(token) && extractUsername(token).equals(username);
    }
}