package com.aniket.placementcell.service;

import com.aniket.placementcell.dto.ApiResponse;
import com.aniket.placementcell.dto.PlacementOfficerRequestDTO;
import com.aniket.placementcell.entity.PlacementOfficer;
import com.aniket.placementcell.entity.User;
import com.aniket.placementcell.repository.PlacementOfficerRepository;
import com.aniket.placementcell.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlacementOfficerService {
    private final PlacementOfficerRepository placementOfficerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public PlacementOfficerService(PlacementOfficerRepository placementOfficerRepository,
                        UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        EmailService emailService) {
        this.placementOfficerRepository = placementOfficerRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public ApiResponse<String> registerOfficer(PlacementOfficerRequestDTO dto) {
        try {
            System.out.println("[SERVICE] Starting registration for: " + dto.getEmail());

            // Check if user already exists
            if (userRepository.existsByUsername(dto.getEmail())) {
                return new ApiResponse<>(false, "Officer Email id already present!",
                        "The email " + dto.getEmail() + " is already registered",
                        dto.getEmail());
            }

            // Create user account
            User user = new User();
            user.setUsername(dto.getEmail());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setRole(dto.getRole());
            User savedUser = userRepository.save(user);
            System.out.println("[SERVICE] User account created: " + savedUser.getUsername());

            // Create placement officer profile
            PlacementOfficer officer = new PlacementOfficer();
            officer.setName(dto.getName());
            officer.setEmail(dto.getEmail());
            officer.setPosition(dto.getPosition());
            officer.setMobileNumber(dto.getMobileNumber());
            officer.setPhotoUrl(dto.getPhotoUrl());
            officer.setRole(dto.getRole());
            officer.setActive(dto.isActive());
            officer.setUser(savedUser);

            PlacementOfficer savedOfficer = placementOfficerRepository.save(officer);
            System.out.println("[SERVICE] PlacementOfficer saved: " + savedOfficer.getName());

            // Send welcome email with proper error handling
            // String emailStatus = sendWelcomeEmail(dto.getEmail(), dto.getName());

            System.out.println("[SERVICE] Officer registration completed successfully");

            return new ApiResponse<>(true, "Successfully added the officer!",
                    "Officer " + dto.getName() + " has been registered. " +
                            dto.getEmail());

        } catch (Exception e) {
            System.out.println("[SERVICE] Registration failed: " + e.getMessage());
            return new ApiResponse<>(false, "Registration failed!",
                    "Error: " + e.getMessage(),
                    null);
        }
    }

    private String sendWelcomeEmail(String email, String name) {
        try {
            emailService.sendWelcomeOfficerMail(email, name);
            return "Welcome email sent successfully.";
        } catch (Exception e) {
            System.out.println("[SERVICE] Email failed but registration successful: " + e.getMessage());
            // Don't fail the registration if email fails
            return "Registration completed but email could not be sent: " + e.getMessage();
        }
    }

    public Page<PlacementOfficer> getOfficers(Pageable pageable)
    {
return placementOfficerRepository.findAll(pageable);
    }
    public long getTotalOfficersCount()
    {
       return placementOfficerRepository.count();
    }
    public int getActiveOfficersCount()
    {
       return placementOfficerRepository.countByIsActive(true);
    }
    public int getInactiveOfficersCount()
    {
      return  placementOfficerRepository.countByIsActive(false);
    }
}
