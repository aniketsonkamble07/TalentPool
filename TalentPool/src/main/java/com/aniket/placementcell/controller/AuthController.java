package com.aniket.placementcell.controller;

import com.aniket.placementcell.dto.JwtResponse;
import com.aniket.placementcell.dto.LoginDTO;
import com.aniket.placementcell.enums.Role;
import com.aniket.placementcell.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            System.out.println("\nLogin attempt for: " + loginDTO.getUsername());

            // Authenticate user credentials
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    )
            );

            System.out.println("Authentication successful: " + authentication.isAuthenticated());

            // Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getUsername());
            System.out.println("Loaded UserDetails: " + userDetails.getUsername());
            System.out.println("Authorities: " + userDetails.getAuthorities());

            // Extract role from authorities
            String roleStr = extractRoleFromAuthorities(userDetails);
            Role role = Role.valueOf(roleStr);
            System.out.println("Converted role: " + role);

            // Generate JWT token
            String token = jwtUtil.generateToken(loginDTO.getUsername(), role);

            // CRITICAL: Log the actual token that should be used
            System.out.println("Login successful for: " + loginDTO.getUsername());
            System.out.println("GENERATED TOKEN: " + token);
            System.out.println("TOKEN LENGTH: " + token.length());
            System.out.println("COPY THIS TOKEN FOR YOUR REQUESTS!");

            return ResponseEntity.ok(new JwtResponse(token));

        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }
    }
    @PostMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            System.out.println("\n Verifying token...");

            if (token == null || token.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Token is required"));
            }

            boolean isValid = jwtUtil.validateToken(token);

            if (isValid) {
                String username = jwtUtil.extractUsername(token);
                Role role = jwtUtil.extractRole(token);

                System.out.println("Token valid for: " + username + " | Role: " + role);

                return ResponseEntity.ok(Map.of(
                        "valid", true,
                        "username", username,
                        "role", role,
                        "expiresAt", jwtUtil.extractExpiration(token)
                ));
            } else {
                System.out.println("Token invalid");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("valid", false, "error", "Invalid token"));
            }
        } catch (Exception e) {
            System.out.println("Token verification failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("valid", false, "error", e.getMessage()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok(Map.of("status", "Auth service is running"));
    }

    private String extractRoleFromAuthorities(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No role assigned"))
                .getAuthority()
                .replace("ROLE_", "");
    }
}

/*


@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    // ----------------------------
    // LOGIN PAGE
    // ----------------------------
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }

    // ----------------------------
    // LOGIN PROCESS
    // ----------------------------
    @PostMapping("/login")
    public String doLogin(
            @Valid @ModelAttribute LoginDTO loginDTO,
            Model model,
            HttpServletResponse response) {

        try {
            System.out.println("\n[LOGIN] Attempt for: " + loginDTO.getUsername());

            // Authenticate user credentials
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    )
            );

            System.out.println("[LOGIN] Authentication success.");

            //  Extract authority like ROLE_STUDENT
            String authority = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Role missing"));

            System.out.println("[LOGIN] Spring Authority: " + authority);

            // Convert to Enum: STUDENT | ADMIN | PLACEMENT_OFFICER
            String enumName = authority.replace("ROLE_", "").toUpperCase();
            Role role = Role.valueOf(enumName);

            System.out.println("[LOGIN] Enum Role: " + role);

            // Generate JWT token
            String token = jwtUtil.generateToken(loginDTO.getUsername(), role);
            System.out.println("[TOKEN] Generated JWT: " + token);

            // Store JWT in HttpOnly cookie

            Cookie jwtCookie = new Cookie("jwt", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(60 * 60); // 1 hour
            if (!isLocalhost()) {
                jwtCookie.setSecure(true);
            }
            // On localhost, secure=false. On prod, secure=true.
            if (!isLocalhost()) {
                jwtCookie.setSecure(true);
            }

            response.addCookie(jwtCookie);

            //  Redirect based on role
            return determineRedirectUrl(authority);

        } catch (Exception e) {
            System.out.println("[LOGIN] Failed: " + e.getMessage());
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    // ✔ Development: always return false
    private boolean isLocalhost() {
        return true; // change in production
    }

    // ----------------------------
    // ROLE-BASED REDIRECTION
    // ----------------------------
    private String determineRedirectUrl(String authority) {

        switch (authority) {
            case "ROLE_ADMIN":
                return "redirect:/dashboard";

            case "ROLE_PLACEMENT_OFFICER":
                return "redirect:/admin/dashboard";

            case "ROLE_STUDENT":
                return "redirect:/student/home";

            default:
                return "redirect:/jobs";
        }
    }
}

*/