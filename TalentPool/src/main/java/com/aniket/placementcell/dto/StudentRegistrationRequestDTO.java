package com.aniket.placementcell.dto;

import com.aniket.placementcell.enums.Branch;
import com.aniket.placementcell.enums.Gender;
import com.aniket.placementcell.enums.PlacementStatus;
import com.aniket.placementcell.enums.StudentYear;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentRegistrationRequestDTO {

    @NotNull(message = "CRN number is required")
    private Long crnNumber;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{6,}$",
            message = "Password must contain uppercase, lowercase, digit and be at least 6 characters long"
    )
    private String password;

    @NotNull(message = "Branch is required")
    private Branch branch;

    @NotNull(message = "Year is required")
    private StudentYear year;

    @NotNull(message = "Passing year is required")
    @Min(value = 2000, message = "Passing year must be valid")
    private Integer passingYear;

    @NotBlank(message = "Mobile number is required")
    @Pattern(
            regexp = "^[0-9]{10,15}$",
            message = "Mobile number must be 10 to 15 digits"
    )
    private String mobileNumber;

    @PositiveOrZero(message = "CGPA cannot be negative")
    @DecimalMax(value = "10", message = "CGPA cannot exceed 10")
    private Double cgpa;

    @PositiveOrZero(message = "10th marks must be at least 0")
    @DecimalMax(value = "100", message = "10th marks cannot exceed 100")
    private Double mark10th;

    @PositiveOrZero(message = "12th marks must be at least 0")
    @DecimalMax(value = "100", message = "12th marks cannot exceed 100")
    private Double mark12th;

    @PositiveOrZero(message = "Diploma marks must be at least 0")
    @DecimalMax(value = "100", message = "Diploma marks cannot exceed 100")
    private Double diplomaMarks;

    @PositiveOrZero(message = "Aggregate marks must be at least 0")
    @DecimalMax(value = "100", message = "Aggregate marks cannot exceed 100")
    private Double aggregateMarks;

    private Boolean yearDown;

    @PositiveOrZero(message = "Active backlog cannot be negative")
    private Integer activeBacklog;

    private Gender gender;

    @Size(max = 500, message = "Remarks cannot exceed 500 characters")
    private String remarks;



    private PlacementStatus placementStatus;
    private String companyName;

    private Double salary;
}
