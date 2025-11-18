package com.aniket.placementcell.dto;

import com.aniket.placementcell.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlacementOfficerResponseDTO {

    private String name;
    private String email;
    private String position;
    private String mobileNumber;
    private String photoUrl;
    private Role role;
    private boolean active;
}
