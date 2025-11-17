package com.aniket.placementcell.repository;

import com.aniket.placementcell.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByCrnNumber(Long crnNumber);
    boolean existsByEmail(String email);
    Optional<Student> findByCrnNumber(Long crnNumber);
    Optional<Student> findByEmail(String email);
}
