package com.aniket.placementcell.controller;

import com.aniket.placementcell.dto.JobPostingResponseDTO;
import com.aniket.placementcell.dto.StudentResponseDTO;
import com.aniket.placementcell.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService service;

    @GetMapping("/home")
    public String studentHomePage(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("Unauthenticated access to /student/home");
            return "redirect:/auth/login";
        }

        String user = authentication.getName();
        System.out.println("Authenticated user: " + user);

        StudentResponseDTO student = service.sendProfile(user);
        System.out.println("StudentResponseDTO retrieved: " + student);

        List<JobPostingResponseDTO> jobs = service.getAllJobPosting();
        System.out.println("Total jobs retrieved: " + jobs.size());

        model.addAttribute("student", student);
        model.addAttribute("jobs", jobs);

        return "student_home";
    }

    @GetMapping("/job/details/{jobId}")
    public String showJobDetails(@PathVariable String jobId, Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("Unauthenticated access to /student/job/details/" + jobId);
            return "redirect:/auth/login";
        }

        System.out.println("Fetching details for job ID: " + jobId);
        JobPostingResponseDTO job = service.getJobPosting(jobId);
        System.out.println("JobPostingResponseDTO retrieved: " + job);

        model.addAttribute("job", job);

        return "job_details";
    }
}
