package com.aniket.placementcell.dto;

import com.aniket.placementcell.enums.Branch;
import com.aniket.placementcell.enums.EmploymentType;
import com.aniket.placementcell.enums.JobStatus;
import com.aniket.placementcell.enums.JobType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class JobPostingRequestDTO {

    @NotBlank(message = "Job title is required")
    @Size(max = 200, message = "Job title must not exceed 200 characters")
    private String title;

    @NotBlank(message = "Job id is required")
    @Size(max = 10, message = "Job id must not exceed 10 characters")
    private String jobId;

    private String description;

    @NotNull(message = "Job type required!!")
    private JobType jobType;

    @NotNull(message = "Employment type required!!")
    private EmploymentType employmentType;

    @NotBlank(message = "Location is required")
    private String location;

    @DecimalMin(value = "0.0", message = "Salary must be positive")
    private Double minSalary;

    @DecimalMin(value = "0.0", message = "Salary must be positive")
    private Double maxSalary;

    @NotBlank(message = "Salary type is required")
    private String salaryType; // "PER_ANNUM", "PER_MONTH", "PER_HOUR"

    @DecimalMin(value = "0.0", inclusive = true, message = "CGPA cannot be less than 0.0")
    @DecimalMax(value = "10.0", inclusive = true, message = "CGPA cannot exceed 10.0")
    private Double requiredCGPA;

    @NotNull(message = "Required skills are required!!")
    private List<String> requiredSkills;

    @NotNull(message = "Required branches are required!!")
    private List<Branch> requiredBranches;


    @Future(message = "Application deadline must be in the future")
    private LocalDate applicationDeadline;


    @Future(message = "Drive date must be in the future")
    private LocalDate driveDate;

    private String driveTime;

    @NotBlank(message = "Drive venue is required")
    private String driveVenue;

    @Min(value = 1, message = "Vacancies must be at least 1")
    private Integer numberOfVacancies;

    @NotNull(message = "Job status is required")
    private JobStatus status;

    @NotBlank(message = "Company name required")
    @Size(max = 100, message = "Company name must not exceed 100 characters")
    private String companyName;

    // Only include what you need for officer info
    private OfficerInfoDTO postedBy;

}
