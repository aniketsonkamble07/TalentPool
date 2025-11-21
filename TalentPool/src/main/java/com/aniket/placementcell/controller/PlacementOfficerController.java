package com.aniket.placementcell.controller;

import com.aniket.placementcell.dto.ApiResponse;
import com.aniket.placementcell.dto.PlacementOfficerRequestDTO;
import com.aniket.placementcell.entity.PlacementOfficer;
import com.aniket.placementcell.service.PlacementOfficerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


/*
* @PostMapping("/registerofficer")
    public ResponseEntity<?> registerOfficer(@RequestBody PlacementOfficerRequestDTO officerDTO) {

            System.out.println("[DEBUG] Starting registration for officer: " + officerDTO.getEmail());

            // Get current authentication
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("[DEBUG] Current user: " + authentication.getName());
            System.out.println("[DEBUG] Authorities: " + authentication.getAuthorities());


            // Call the service
            ApiResponse<String> response = adminService.registerOfficer(officerDTO);



            if (response.isSuccess()) {
                System.out.println("[DEBUG] Officer registration completed successfully");
                return ResponseEntity.ok(response);
            } else {
                System.out.println("[DEBUG] Officer registration failed: " + response.getMessage());
                return ResponseEntity.badRequest().body(response);
            }
    }
* */


@Controller
@RequestMapping("/admin")
public class PlacementOfficerController {

    @Autowired
    private PlacementOfficerService placementOfficerService;



    @GetMapping("/officers/register")
    public String registerOfficer(Model model) {
        model.addAttribute("placementOfficerRequestDTO", new PlacementOfficerRequestDTO());
        return "addPlacementOfficer"; // Make sure this matches your HTML file name
    }

    @PostMapping("/officers/register")
    public String registerOfficer(
            @Valid @ModelAttribute("placementOfficerRequestDTO") PlacementOfficerRequestDTO officerDTO,
            BindingResult bindingResult,
            Model model) {

        System.out.println("[DEBUG] Starting registration for officer: " + officerDTO.getEmail());
        System.out.println("[DEBUG] Officer DTO: " + officerDTO);

        // Check for validation errors
        if (bindingResult.hasErrors()) {
            System.out.println("[DEBUG] Validation errors found: " + bindingResult.getAllErrors());
            // Return to form with error messages - data will be preserved
            return "addPlacementOfficer";
        }

        try {
            // current user details (optional)
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("[DEBUG] Current user: " + authentication.getName());
            System.out.println("[DEBUG] Authorities: " + authentication.getAuthorities());

            ApiResponse<String> response = placementOfficerService.registerOfficer(officerDTO);

            if (response.isSuccess()) {
                model.addAttribute("success", "Officer registered successfully!");
                // Clear the form by adding a new DTO
                model.addAttribute("placementOfficerRequestDTO", new PlacementOfficerRequestDTO());
                return "redirect:/admin/dashboard"; // Return to same form with success message
            } else {
                model.addAttribute("error", response.getMessage());
                return "addPlacementOfficer"; // Return to same form with error
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Exception during officer registration: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "addPlacementOfficer"; // Return to same form with error
        }
    }


    @GetMapping("officers/list")
    public String officersList(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(required = false) String search,
                               @RequestParam(required = false) String status,

                               Model model)
    {
        Pageable pageable = PageRequest.of(0, 10);
        Page<PlacementOfficer> officersPage = placementOfficerService.getOfficers(pageable);

        // Add data to model
        model.addAttribute("pageTitle", "Officer Management");
        model.addAttribute("activePage", "officers");
        model.addAttribute("officers", officersPage);

        // Statistics
        model.addAttribute("totalOfficers", placementOfficerService.getTotalOfficersCount());
        model.addAttribute("activeOfficers", placementOfficerService.getActiveOfficersCount());
        model.addAttribute("inactiveOfficers",placementOfficerService.getInactiveOfficersCount());
        return "officer_list";
    }
}
