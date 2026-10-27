package com.ems.employeemanagementsystem.exception;

/**
 * Thrown when attempting to register or update an employee with an email that already exists.
 */
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String message) {
        super(message);
    }
}
