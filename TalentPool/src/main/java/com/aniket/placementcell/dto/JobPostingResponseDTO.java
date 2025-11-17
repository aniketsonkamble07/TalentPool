package com.aniket.placementcell.dto;

import com.aniket.placementcell.enums.Branch;
import com.aniket.placementcell.enums.EmploymentType;
import com.aniket.placementcell.enums.JobStatus;
import com.aniket.placementcell.enums.JobType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Data
@Getter
@Setter
public class JobPostingResponseDTO {
    private String title;
    private String jobId;
    private String description;
    private JobType jobType;
    private EmploymentType employmentType;
    private String location;
    private Double minSalary;
    private Double maxSalary;
    private String salaryType; // "PER_ANNUM", "PER_MONTH", "PER_HOUR"
    private Double requiredCGPA;
    private List<String> requiredSkills;
    private List<Branch> requiredBranches;
    private LocalDate applicationDeadline;
    private LocalDate driveDate;
    private String driveTime;
    private String driveVenue;
    private Integer numberOfVacancies;
    private JobStatus status;
    private String companyName;
    private String postedByName;
    private String postedByEmail;
    private Integer viewsCount;
    private Integer applicationsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;
    private boolean applied;
    private boolean applicationDeadlinePassed;
}






