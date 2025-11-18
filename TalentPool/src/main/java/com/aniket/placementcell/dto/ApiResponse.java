package com.aniket.placementcell.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private String description;
    private T data; // Add this missing field

    // Constructor without data (for backward compatibility)
    public ApiResponse(boolean success, String message, String description) {
        this.success = success;
        this.message = message;
        this.description = description;
        this.data = null;
    }

    // Constructor with data
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.description = message; // Use message as description
        this.data = data;
    }

    // Constructor with all fields
    public ApiResponse(boolean success, String message, String description, T data) {
        this.success = success;
        this.message = message;
        this.description = description;
        this.data = data;
    }
}