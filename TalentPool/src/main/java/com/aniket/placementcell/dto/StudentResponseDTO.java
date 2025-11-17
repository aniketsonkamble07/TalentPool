package com.aniket.placementcell.dto;

import com.aniket.placementcell.enums.Branch;
import com.aniket.placementcell.enums.Gender;
import com.aniket.placementcell.enums.PlacementStatus;
import com.aniket.placementcell.enums.StudentYear;
import lombok.Data;

import java.util.List;

@Data
public class StudentResponseDTO {
    private  String name;
    private Long crnNumber;
    private String email;
    private Branch branch;
    private StudentYear studentYear;
    private String mobileNumber;
    private Double cgpa;
    private Double mark10th;
    private Double mark12th;
    private Double diplomaMark;
    private Double aggregateMark;
    private int activeBacklog;
    private Integer yearDown;
    private PlacementStatus placementStatus;

    private Gender gender;
    private String companyName;
    private  Double salary;

    private List<AppliedDTO> appliedDTOList;

}
