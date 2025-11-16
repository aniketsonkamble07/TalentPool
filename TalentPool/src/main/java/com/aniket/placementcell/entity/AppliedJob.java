package com.aniket.placementcell.entity;

import com.aniket.placementcell.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "applied_jobs")
public class AppliedJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many applications can be for one job posting
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_posting_id")
    private JobPosting jobPosting;

    // Many applications can be from one student
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    private LocalDateTime appliedDate;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    // Constructors, getters, and setters
    public AppliedJob() {
    }

    public AppliedJob(JobPosting jobPosting, Student student, LocalDateTime appliedDate, ApplicationStatus status) {
        this.jobPosting = jobPosting;
        this.student = student;
        this.appliedDate = appliedDate;
        this.status = status;
    }
}