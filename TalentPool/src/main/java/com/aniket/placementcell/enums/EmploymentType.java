package com.aniket.placementcell.enums;

public enum EmploymentType {
    ON_SITE("On-Site"),
    REMOTE("Remote"),
    HYBRID("Hybrid");

    private final String displayName;

    EmploymentType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
