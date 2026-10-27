package com.ems.employeemanagementsystem.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for Employee form binding and validation.
 * Isolates Web Layer form submissions from the persistence Entity.
 */
public class EmployeeDto {

    private Long id;

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s.'-]+$", message = "Full name can only contain letters, spaces, hyphens, and apostrophes")
    private String fullName;

    @NotBlank(message = "Email address is required")
    @Email(message = "Please provide a valid email address (e.g., user@company.com)")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(
        regexp = "^[+]?[0-9\\s().-]{7,20}$",
        message = "Please enter a valid phone number (7 to 20 characters: digits, spaces, hyphens, parentheses, optional '+')"
    )
    private String phone;

    @NotBlank(message = "Please select a department")
    private String department;

    @NotBlank(message = "Job title is required")
    @Size(min = 2, max = 100, message = "Job title must be between 2 and 100 characters")
    private String jobTitle;

    @NotNull(message = "Salary is required")
    @DecimalMin(value = "0.01", message = "Salary must be greater than zero")
    @Digits(integer = 10, fraction = 2, message = "Salary format must be up to 10 digits and 2 decimal places")
    private BigDecimal salary;

    @NotNull(message = "Joining date is required")
    @PastOrPresent(message = "Joining date cannot be a future date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate joiningDate;

    public EmployeeDto() {
    }

    public EmployeeDto(Long id, String fullName, String email, String phone,
                       String department, String jobTitle, BigDecimal salary,
                       LocalDate joiningDate) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.jobTitle = jobTitle;
        this.salary = salary;
        this.joiningDate = joiningDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }
}
