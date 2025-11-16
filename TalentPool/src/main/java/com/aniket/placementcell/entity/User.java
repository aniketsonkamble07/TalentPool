package com.aniket.placementcell.entity;

import com.aniket.placementcell.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {
    @Id
    private String username; // Use email as username

    private String password;
    private Role role;


}
