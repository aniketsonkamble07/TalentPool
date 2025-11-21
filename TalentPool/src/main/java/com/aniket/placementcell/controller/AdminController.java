package com.aniket.placementcell.controller;

import com.aniket.placementcell.dto.ApiResponse;
import com.aniket.placementcell.dto.PlacementOfficerRequestDTO;
import com.aniket.placementcell.entity.PlacementOfficer;
import com.aniket.placementcell.entity.Student;
import com.aniket.placementcell.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

@GetMapping("/students/list")
public Page studentList(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size)
{
    Pageable pageable=PageRequest.of(0,10);
    Page<Student> students=adminService.studentList(pageable);
    return students;
}



}


/*
@Controller
@RequestMapping("/admin")
public class AdminController
{
    @Autowired
    private AdminService adminService;

    @GetMapping("/dashboard")
    public String dashboard(Model model)
    {
        return "admin_dashboard";
    }

    @GetMapping("/students/list")
    public String studentList(Model model)
    {
        Pageable pageable = PageRequest.of(0, 10);
      Page<Student> students=  adminService.studentList(pageable);
         model.addAttribute("students",students);
        return "student_list";
    }
    @GetMapping("students/list/{branch}")
    public  String studentListByBranch(@PathVariable String branch, Model model)
    {
        Pageable pageable=PageRequest.of(0,10);
      Page<Student>  students=adminService.getByBranch(branch, pageable);
      model.addAttribute("students",students);
      return "student_list";
    }
}
*/