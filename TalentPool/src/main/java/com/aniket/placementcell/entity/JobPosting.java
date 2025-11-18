package com.aniket.placementcell.entity;

import com.aniket.placementcell.enums.Branch;
import com.aniket.placementcell.enums.EmploymentType;
import com.aniket.placementcell.enums.JobStatus;
import com.aniket.placementcell.enums.JobType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "job_postings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Job title is required")
    @Size(max = 200, message = "Job title must not exceed 200 characters")
    @Column(nullable = false, length = 200)
    private String title;

    @NotBlank(message="Job id is required")
    @Size(max=10, message="Job id  not exceed 10 character")
    private String jobId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EmploymentType employmentType;

    @NotBlank(message = "Location is required")
    @Column(nullable = false, length = 100)
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




    @ElementCollection
    @CollectionTable(name = "job_required_skills", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "skill")
    private List<String> requiredSkills;

    @ElementCollection
    @CollectionTable(name = "allowed_branches", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "allowed_branches")
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private JobStatus status;

    @NotBlank(message = "Company name required")
    @Size(max=100, message = "company name not exceed 100 character!!")
    private String companyName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posted_by")
    private PlacementOfficer postedBy;
    @Column(name = "views_count")
    private Integer viewsCount = 0;

    @Column(name = "applications_count")
    private Integer applicationsCount = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;


    @OneToMany(mappedBy = "jobPosting", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AppliedJob> applications;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == JobStatus.PUBLISHED) {
            publishedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        if (status == JobStatus.PUBLISHED && publishedAt == null) {
            publishedAt = LocalDateTime.now();
        }
    }

    // Helper methods
    public boolean isActive() {
        return status == JobStatus.PUBLISHED &&
                applicationDeadline != null &&
                applicationDeadline.isAfter(LocalDate.now());
    }

    public boolean canApply() {
        return isActive() &&
                (numberOfVacancies == null || numberOfVacancies > applicationsCount);
    }

    public String getSalaryRange() {
        if (minSalary != null && maxSalary != null) {
            return String.format("₹%.1f - ₹%.1f %s", minSalary, maxSalary, getSalaryTypeDisplay());
        } else if (minSalary != null) {
            return String.format("₹%.1f %s", minSalary, getSalaryTypeDisplay());
        } else if (maxSalary != null) {
            return String.format("Up to ₹%.1f %s", maxSalary, getSalaryTypeDisplay());
        }
        return "Not disclosed";
    }

    private String getSalaryTypeDisplay() {
        switch (salaryType) {
            case "PER_ANNUM": return "LPA";
            case "PER_MONTH": return "per month";
            case "PER_HOUR": return "per hour";
            default: return "";
        }
    }
}





