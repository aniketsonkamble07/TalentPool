package com.aniket.placementcell.repository;

import com.aniket.placementcell.entity.JobPosting;
import com.aniket.placementcell.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobPostRepository extends JpaRepository<JobPosting, Long> {

    boolean existsByJobId(String jobId);


    List<JobPosting> findByStatusOrderByCreatedAtDesc(JobStatus status);

    Optional<JobPosting> findByJobId(String jobId);



    // Find recommended jobs based on student's branch and CGPA
    @Query("SELECT j FROM JobPosting j WHERE j.status = com.aniket.placementcell.enums.JobStatus.PUBLISHED AND j.applicationDeadline >= CURRENT_DATE ORDER BY j.createdAt DESC")
    List<JobPosting> findActiveJobsWithValidDeadline();

    @Query("SELECT j FROM JobPosting j WHERE j.status = com.aniket.placementcell.enums.JobStatus.PUBLISHED AND j.applicationDeadline >= CURRENT_DATE " +
            "AND (j.requiredBranches IS EMPTY OR :branch MEMBER OF j.requiredBranches) " +
            "AND (j.requiredCGPA IS NULL OR j.requiredCGPA <= :cgpa) " +
            "ORDER BY j.createdAt DESC")
    List<JobPosting> findRecommendedJobs(@Param("branch") String branch, @Param("cgpa") Double cgpa);

}

