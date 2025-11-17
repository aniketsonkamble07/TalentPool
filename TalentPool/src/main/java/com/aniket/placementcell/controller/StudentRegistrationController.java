package com.aniket.placementcell.controller;

import com.aniket.placementcell.dto.ApiResponse;
import com.aniket.placementcell.dto.StudentRegistrationRequestDTO;
import com.aniket.placementcell.security.JwtUtil;
import com.aniket.placementcell.service.StudentRegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
@RestController
@RequestMapping("/student")
public class StudentRegistrationController {

    @Autowired
   private StudentRegistrationService service;

    @Autowired
   private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerPage(@RequestBody @Valid StudentRegistrationRequestDTO studentRegistrationRequestDTO)
    {
        ApiResponse response=service.registerStudent(studentRegistrationRequestDTO);
        if (response.isSuccess()) {
            String token = jwtUtil.generateToken(studentRegistrationRequestDTO.getEmail());
            // Add token to response description or data
            response.setDescription("JWT Token: " + token);
        }

        // Return proper HTTP status
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }

    }
}
*/

@Controller
@RequestMapping("/student")
public class StudentRegistrationController {

    @Autowired
    private StudentRegistrationService service;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/register")
    public String registrationPage(Model model)
    {
        model.addAttribute("studentRequestDTO", new StudentRegistrationRequestDTO());
        return "register";
    }


    @PostMapping("/register")
    public String registrationPage(
            @ModelAttribute("studentRequestDTO") @Valid StudentRegistrationRequestDTO studentRegistrationRequestDTO,
            BindingResult bindingResult,
            Model model,
            HttpServletResponse response,
            HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("success", false);
            model.addAttribute("message", "Please correct the errors in the form");
            return "register";
        }

        ApiResponse<?> apiResponse = service.registerStudent(studentRegistrationRequestDTO);

        if (apiResponse.isSuccess()) {

            // 1. Create Spring session authentication
            List<GrantedAuthority> authorities =
                    List.of(new SimpleGrantedAuthority("ROLE_STUDENT"));

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            studentRegistrationRequestDTO.getEmail(),
                            null,
                            authorities
                    );

            SecurityContextHolder.getContext().setAuthentication(authToken);

            // 2. Create HTTP Session (required!)
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());

            return "redirect:/student/home";
        } else {
            model.addAttribute("success", false);
            model.addAttribute("message", apiResponse.getMessage());
            model.addAttribute("description", apiResponse.getDescription());
            return "register";
        }
    }



}
