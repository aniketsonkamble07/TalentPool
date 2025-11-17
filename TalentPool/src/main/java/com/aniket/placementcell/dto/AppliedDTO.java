package com.aniket.placementcell.dto;

import com.aniket.placementcell.enums.Branch;
import com.aniket.placementcell.enums.EmploymentType;
import com.aniket.placementcell.enums.JobStatus;
import com.aniket.placementcell.enums.JobType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AppliedDTO {

    //  Basic Job Info
    private Long jobId;
    private String title;
    private String description;
    private String companyName;

    //  Job Details
    private JobType jobType;
    private EmploymentType employmentType;
    private String location;
    private Double minSalary;
    private Double maxSalary;
    private String salaryType; // PER_ANNUM, PER_MONTH, PER_HOUR
    private Double requiredCGPA;
    private List<String> requiredSkills;
    private List<Branch> requiredBranches;

    // Drive & Vacancy Info
    private LocalDate applicationDeadline;
    private LocalDate driveDate;
    private String driveTime;
    private String driveVenue;
    private Integer numberOfVacancies;
    private JobStatus status;

    // Meta Information
    private String postedBy; // Placement officer name instead of object reference
    private Integer viewsCount;
    private Integer applicationsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;

    //  Application Info (student specific)
    private LocalDateTime appliedDate;
    private String applicationStatus; // e.g., "APPLIED", "SHORTLISTED", "REJECTED", etc.
}