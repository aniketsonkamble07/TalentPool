package com.aniket.placementcell.controller;


import com.aniket.placementcell.dto.JobPostingRequestDTO;
import com.aniket.placementcell.entity.PlacementOfficer;
import com.aniket.placementcell.repository.PlacementOfficerRepository;
import com.aniket.placementcell.service.JobPostingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/jobs/")
public class JobPostingController {

    @Autowired
    private JobPostingService service;

    @Autowired
    private PlacementOfficerRepository officerRepository;

    @GetMapping("/addPost")
    public String showJobForm(Model model) {
        model.addAttribute("jobPostingRequestDTO", new JobPostingRequestDTO());
        return "addJobPost";
    }

    @PostMapping("/addPost")
    public String addJobPost(@Valid @ModelAttribute JobPostingRequestDTO dto,
                             Authentication authentication,
                             Model model) {

        String username = authentication.getName();
        PlacementOfficer officer = officerRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Officer not found: " + username));

        service.addJobPost(dto, officer);

        model.addAttribute("success", "Job added successfully!");
        return "redirect:/admin/dashboard"; // or redirect:/jobs/add if you want to refresh
    }

}




