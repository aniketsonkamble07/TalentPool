package com.aniket.placementcell.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 🔹 Handle AlreadyPresentException (duplicate CRN or Email)
    @ExceptionHandler(AlreadyPresentException.class)
    public ResponseEntity<APIError> handleAlreadyPresentException(AlreadyPresentException ex) {
        APIError error = new APIError(
                HttpStatus.CONFLICT.value(), // 409
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // 🔹 Handle Resource Not Found (404 errors)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIError> handleResourceNotFoundException(ResourceNotFoundException ex) {
        APIError error = new APIError(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // 🔹 Handle favicon.ico requests specifically
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIError> handleGenericException(Exception ex, HttpServletRequest request) {

        // Check if it's a favicon request
        if (request.getRequestURI().endsWith("/favicon.ico")) {
            // Return 404 instead of 500 for favicon
            APIError error = new APIError(
                    HttpStatus.NOT_FOUND.value(),
                    "Favicon not found"
            );
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        // For all other exceptions, return 500
        APIError error = new APIError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Something went wrong! " + ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
