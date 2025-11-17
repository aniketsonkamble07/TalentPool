package com.aniket.placementcell.entity;

import com.aniket.placementcell.enums.Branch;
import com.aniket.placementcell.enums.Gender;
import com.aniket.placementcell.enums.PlacementStatus;
import com.aniket.placementcell.enums.StudentYear;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Student {

    @Id
    @Column(name = "crn_number", unique = true, nullable = false)
    private Long crnNumber;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Branch branch;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudentYear year;

    @Column(name = "passing_year", nullable = false)
    private Integer passingYear;

    @Column(name = "mobile_number", nullable = false, length = 15)
    private String mobileNumber;

    private Double cgpa;

    @Column(name = "mark10th")
    private Double mark10th;

    @Column(name = "mark12th")
    private Double mark12th;

    @Column(name = "diploma_marks")
    private Double diplomaMarks;

    @Column(name = "aggregate_marks")
    private Double aggregateMarks;

    @Column(name = "year_down")
    private Boolean yearDown;

    @Column(name = "active_backlog")
    private Integer activeBacklog;

    @Enumerated(EnumType.STRING)
    @Column(name = "placement_status", nullable = false)
    private PlacementStatus placementStatus;

    @Column(length = 500)
    private String remarks;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "company_name")
    private String companyName;

    private Double salary;

    @CreatedDate
    @Column(name = "register_time", updatable = false)
    private LocalDateTime registerTime;




    @OneToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AppliedJob> appliedList;
}
