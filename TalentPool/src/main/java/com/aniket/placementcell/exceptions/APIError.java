package com.aniket.placementcell.exceptions;


import java.time.LocalDateTime;

public class APIError {

    private int status;
    private String message;
    private LocalDateTime timestamp;

    // 🔹 Constructors
    public APIError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();  // Auto-sets time
    }

    public APIError(int status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    // 🔹 Getters & Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

