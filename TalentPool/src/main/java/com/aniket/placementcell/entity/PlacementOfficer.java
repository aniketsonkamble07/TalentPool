package com.aniket.placementcell.entity;

import com.aniket.placementcell.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlacementOfficer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message="Full name is required")
    @Size(max = 100, message = "Job title must not exceed 200 characters")
    @Column(nullable=false, length=100)
    String  name;

    @NotBlank(message="Position is required")
    @Size(max=100, message="Position must not exceed 100 characters")
    @Column(nullable = false, length = 100)
    String position;

    @NotBlank(message ="Email is required")
    @Column(nullable = false,length = 30,unique = true)
    String email;


    @NotBlank(message = "Mobile numbers is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    @Column(length = 10)
    String mobileNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(length = 500)
    private String photoUrl;

    @Column(nullable = false)
    private  boolean isActive;

    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    public boolean canManageCompanies() {
        return role == Role.PLACEMENT_OFFICER ||
                role == Role.HEAD_PLACEMENT ||
                role == Role.INDUSTRY_RELATIONS_MANAGER;
    }

    public boolean canPostJobs() {
        return role == Role.PLACEMENT_OFFICER ||
                role == Role.HEAD_PLACEMENT ;

    }

    public boolean canManageStudents() {
        return role == Role.PLACEMENT_OFFICER ||
                role == Role.HEAD_PLACEMENT ||
                role == Role.STUDENT_COORDINATOR;
    }
    @OneToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;
}