package com.ems.employeemanagementsystem.service.impl;

import com.ems.employeemanagementsystem.dto.EmployeeDto;
import com.ems.employeemanagementsystem.entity.Employee;
import com.ems.employeemanagementsystem.exception.DuplicateEmailException;
import com.ems.employeemanagementsystem.exception.ResourceNotFoundException;
import com.ems.employeemanagementsystem.repository.EmployeeRepository;
import com.ems.employeemanagementsystem.service.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Service implementation containing transactional business logic.
 * Employs constructor injection for loose coupling and testability.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    // Standard list of company departments
    private static final List<String> DEFAULT_DEPARTMENTS = List.of(
            "Engineering",
            "Human Resources",
            "Finance",
            "Marketing",
            "Sales",
            "Operations",
            "Product Management",
            "Legal",
            "Customer Support"
    );

    // Constructor-based Dependency Injection
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> searchAndFilter(String keyword, String department) {
        String cleanKeyword = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        String cleanDepartment = (department != null && !department.trim().isEmpty() && !department.equalsIgnoreCase("All")) ? department.trim() : null;

        if (cleanKeyword == null && cleanDepartment == null) {
            return employeeRepository.findAll();
        }
        return employeeRepository.searchAndFilter(cleanKeyword, cleanDepartment);
    }

    @Override
    @Transactional(readOnly = true)
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
    }

    @Override
    @Transactional
    public Employee createEmployee(EmployeeDto employeeDto) {
        // Enforce business rule: Unique email address
        if (employeeRepository.existsByEmail(employeeDto.getEmail().trim())) {
            throw new DuplicateEmailException("An employee with email '" + employeeDto.getEmail().trim() + "' already exists.");
        }

        Employee employee = convertToEntity(employeeDto);
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public Employee updateEmployee(Long id, EmployeeDto employeeDto) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        // Enforce business rule: Prevent duplicate email across other employees
        if (employeeRepository.existsByEmailAndIdNot(employeeDto.getEmail().trim(), id)) {
            throw new DuplicateEmailException("An employee with email '" + employeeDto.getEmail().trim() + "' already exists.");
        }

        existing.setFullName(employeeDto.getFullName().trim());
        existing.setEmail(employeeDto.getEmail().trim());
        existing.setPhone(employeeDto.getPhone().trim());
        existing.setDepartment(employeeDto.getDepartment().trim());
        existing.setJobTitle(employeeDto.getJobTitle().trim());
        existing.setSalary(employeeDto.getSalary());
        existing.setJoiningDate(employeeDto.getJoiningDate());

        return employeeRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
        employeeRepository.delete(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isEmailTaken(String email, Long excludeId) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String cleanEmail = email.trim();
        if (excludeId == null) {
            return employeeRepository.existsByEmail(cleanEmail);
        } else {
            return employeeRepository.existsByEmailAndIdNot(cleanEmail, excludeId);
        }
    }

    @Override
    public EmployeeDto convertToDto(Employee employee) {
        if (employee == null) {
            return null;
        }
        return new EmployeeDto(
                employee.getId(),
                employee.getFullName(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getDepartment(),
                employee.getJobTitle(),
                employee.getSalary(),
                employee.getJoiningDate()
        );
    }

    @Override
    public Employee convertToEntity(EmployeeDto dto) {
        if (dto == null) {
            return null;
        }
        return new Employee(
                dto.getId(),
                dto.getFullName() != null ? dto.getFullName().trim() : null,
                dto.getEmail() != null ? dto.getEmail().trim() : null,
                dto.getPhone() != null ? dto.getPhone().trim() : null,
                dto.getDepartment() != null ? dto.getDepartment().trim() : null,
                dto.getJobTitle() != null ? dto.getJobTitle().trim() : null,
                dto.getSalary(),
                dto.getJoiningDate()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalEmployees() {
        return employeeRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public int getTotalDepartmentsCount() {
        return employeeRepository.findDistinctDepartments().size();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalPayroll() {
        List<Employee> all = employeeRepository.findAll();
        return all.stream()
                .map(Employee::getSalary)
                .filter(s -> s != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getAverageSalary() {
        long count = employeeRepository.count();
        if (count == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = getTotalPayroll();
        return total.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getDepartmentStatistics() {
        List<Object[]> results = employeeRepository.countEmployeesByDepartment();
        Map<String, Long> stats = new LinkedHashMap<>();
        for (Object[] row : results) {
            String dept = (String) row[0];
            Long count = (Long) row[1];
            stats.put(dept, count);
        }
        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getRecentHires() {
        return employeeRepository.findTop5ByOrderByJoiningDateDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllDepartments() {
        List<String> dynamicDepts = employeeRepository.findDistinctDepartments();
        if (dynamicDepts.isEmpty()) {
            return DEFAULT_DEPARTMENTS;
        }
        // Merge default departments and any custom dynamic ones while keeping clean sorted order
        java.util.Set<String> set = new java.util.TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        set.addAll(DEFAULT_DEPARTMENTS);
        set.addAll(dynamicDepts);
        return List.copyOf(set);
    }
}
