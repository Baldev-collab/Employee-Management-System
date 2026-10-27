package com.ems.employeemanagementsystem.service;

import com.ems.employeemanagementsystem.dto.EmployeeDto;
import com.ems.employeemanagementsystem.entity.Employee;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Service interface defining core business logic for the Employee Management System.
 */
public interface EmployeeService {

    List<Employee> getAllEmployees();

    List<Employee> searchAndFilter(String keyword, String department);

    Employee getEmployeeById(Long id);

    Employee createEmployee(EmployeeDto employeeDto);

    Employee updateEmployee(Long id, EmployeeDto employeeDto);

    void deleteEmployee(Long id);

    boolean isEmailTaken(String email, Long excludeId);

    EmployeeDto convertToDto(Employee employee);

    Employee convertToEntity(EmployeeDto dto);

    // Dashboard Statistics
    long getTotalEmployees();

    int getTotalDepartmentsCount();

    BigDecimal getTotalPayroll();

    BigDecimal getAverageSalary();

    Map<String, Long> getDepartmentStatistics();

    List<Employee> getRecentHires();

    List<String> getAllDepartments();
}
