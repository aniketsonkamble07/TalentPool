package com.aniket.placementcell.service;

import com.aniket.placementcell.dto.JobPostingRequestDTO;
import com.aniket.placementcell.entity.JobPosting;
import com.aniket.placementcell.entity.PlacementOfficer;
import com.aniket.placementcell.exceptions.AlreadyPresentException;
import com.aniket.placementcell.repository.JobPostRepository;
import com.aniket.placementcell.repository.PlacementOfficerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class JobPostingService {

    @Autowired
    private JobPostRepository repo;
    @Autowired
    PlacementOfficerRepository placementOfficerRepository;

    public void addJobPost(JobPostingRequestDTO dto, PlacementOfficer officer) {
        System.out.println("DEBUG: addJobPost called with DTO: " + dto);
        System.out.println("DEBUG: Officer: " + officer);

        validateJob(dto.getJobId());

        JobPosting job = new JobPosting();
        job.setJobId(dto.getJobId());
        job.setTitle(dto.getTitle());
        job.setDescription(dto.getDescription());
        job.setJobType(dto.getJobType());
        job.setEmploymentType(dto.getEmploymentType());
        job.setLocation(dto.getLocation());

        job.setMinSalary(dto.getMinSalary());
        job.setMaxSalary(dto.getMaxSalary());
        job.setSalaryType(dto.getSalaryType());
        job.setRequiredCGPA(dto.getRequiredCGPA());
        job.setRequiredSkills(dto.getRequiredSkills());
        job.setRequiredBranches(dto.getRequiredBranches());
        job.setApplicationDeadline(dto.getApplicationDeadline());
        job.setDriveDate(dto.getDriveDate());
        job.setDriveTime(dto.getDriveTime());
        job.setDriveVenue(dto.getDriveVenue());
        job.setNumberOfVacancies(dto.getNumberOfVacancies());
        job.setStatus(dto.getStatus());
        job.setCompanyName(dto.getCompanyName());

        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());
        job.setPublishedAt(LocalDateTime.now());
        job.setViewsCount(0);
        job.setApplicationsCount(0);

        System.out.println("DEBUG: Job object before saving: " + job);

        job.setPostedBy(officer);

        repo.save(job);

        System.out.println("DEBUG: Job saved successfully with ID: " + job.getJobId());
    }

    // Validation helper
    private void validateJob(String jobId) {
        System.out.println("DEBUG: Validating jobId: " + jobId);
        if (repo.existsByJobId(jobId)) {
            System.out.println("DEBUG: Job ID already exists: " + jobId);
            throw new AlreadyPresentException(jobId + " is already present");
        }
        System.out.println("DEBUG: Job ID is available: " + jobId);
    }
}
