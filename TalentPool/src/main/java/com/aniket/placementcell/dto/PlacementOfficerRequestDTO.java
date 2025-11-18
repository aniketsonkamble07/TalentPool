package com.aniket.placementcell.dto;

import com.aniket.placementcell.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlacementOfficerRequestDTO {

    @NotBlank(message = "Full name is required")
    private String name;

    @NotBlank(message = "Position is required")
    private String position;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be exactly 10 digits")
    private String mobileNumber;

    @NotBlank(message = "Password is required")
    private String password;

    private String photoUrl;

    private boolean active;

    private Role role;
}