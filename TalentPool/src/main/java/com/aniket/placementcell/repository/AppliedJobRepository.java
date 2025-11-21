package com.aniket.placementcell.repository;

import com.aniket.placementcell.entity.AppliedJob;
import com.aniket.placementcell.entity.JobPosting;
import com.aniket.placementcell.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppliedJobRepository extends JpaRepository<AppliedJob, Long> {

    // Checks if a student has already applied for a specific job
    boolean existsByStudentAndJobPosting(Student student, JobPosting jobPosting);

}
