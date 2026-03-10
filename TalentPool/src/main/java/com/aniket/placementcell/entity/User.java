package com.aniket.placementcell.entity;

import com.aniket.placementcell.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    private String username; // Use email as username

    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;


}
