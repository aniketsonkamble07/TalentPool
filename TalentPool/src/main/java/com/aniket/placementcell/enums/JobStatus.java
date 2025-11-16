package com.aniket.placementcell.enums;

public enum JobStatus {
    DRAFT("Draft"),
    PUBLISHED("Published"),
    CLOSED("Closed"),
    EXPIRED("Expired"),
    CANCELLED("Cancelled");

    private final String displayName;

    JobStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
