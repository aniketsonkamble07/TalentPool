package com.aniket.placementcell.service;

import com.aniket.placementcell.dto.ApiResponse;
import com.aniket.placementcell.dto.PlacementOfficerRequestDTO;
import com.aniket.placementcell.entity.PlacementOfficer;
import com.aniket.placementcell.entity.Student;
import com.aniket.placementcell.entity.User;
import com.aniket.placementcell.repository.PlacementOfficerRepository;
import com.aniket.placementcell.repository.StudentRepository;
import com.aniket.placementcell.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {
@Autowired
private StudentRepository studentRepository;

public Page<Student> studentList(Pageable pageable)
{
    return  studentRepository.findAll(pageable);
}
public Page<Student> getByBranch(String branch, Pageable pageable)
{
    return studentRepository.findByBranch(branch, pageable);
}
}