package com.ems.employeemanagementsystem.exception;

/**
 * Thrown when an employee with a specific ID is not found in the database.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
