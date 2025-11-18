package com.aniket.placementcell.service;

import com.aniket.placementcell.dto.ApiResponse;
import com.aniket.placementcell.dto.StudentRegistrationRequestDTO;
import com.aniket.placementcell.entity.Student;
import com.aniket.placementcell.entity.User;
import com.aniket.placementcell.enums.Role;
import com.aniket.placementcell.repository.StudentRepository;
import com.aniket.placementcell.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Provider;

@Service
public class StudentRegistrationService {

    @Autowired
   private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    public ApiResponse<?>  registerStudent(StudentRegistrationRequestDTO dto)
    {
        if(studentRepository.existsByEmail(dto.getEmail()))
        {
              return new ApiResponse(false, "Username already present",dto.getEmail(),null);
        }

        if(studentRepository.existsByCrnNumber(dto.getCrnNumber()))
        {
            return new ApiResponse(false,"Username already present",dto.getCrnNumber().toString(),null);
        }

        User user = new User();
        user.setUsername(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.STUDENT);
        userRepository.save(user);
        System.out.println("[DEBUG] User account created for: " + user.getUsername());

        Student s = new Student();
        s.setCrnNumber(dto.getCrnNumber());
        s.setName(dto.getName());
        s.setEmail(dto.getEmail());
        s.setBranch(dto.getBranch());
        s.setYear(dto.getYear());
        s.setPassingYear(dto.getPassingYear());
        s.setMobileNumber(dto.getMobileNumber());
        s.setPlacementStatus(dto.getPlacementStatus());
        s.setGender(dto.getGender());
        s.setCgpa(dto.getCgpa());
        s.setMark10th(dto.getMark10th());
        s.setMark12th(dto.getMark12th());
        s.setDiplomaMarks(dto.getDiplomaMarks());
        s.setAggregateMarks(dto.getAggregateMarks());
        s.setYearDown(dto.getYearDown());
        s.setActiveBacklog(dto.getActiveBacklog());
        s.setRemarks(dto.getRemarks());
        s.setCompanyName(dto.getCompanyName());
        s.setSalary(dto.getSalary());

        s.setUser(user); // link to user
        studentRepository.save(s);

        System.out.println("[DEBUG] Student saved with CRN: " + s.getCrnNumber());

        return  new ApiResponse<>(true, "Registration successful !!",null);
    }

}
