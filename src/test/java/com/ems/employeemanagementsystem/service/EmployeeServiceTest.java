package com.ems.employeemanagementsystem.service;

import com.ems.employeemanagementsystem.dto.EmployeeDto;
import com.ems.employeemanagementsystem.entity.Employee;
import com.ems.employeemanagementsystem.exception.DuplicateEmailException;
import com.ems.employeemanagementsystem.exception.ResourceNotFoundException;
import com.ems.employeemanagementsystem.repository.EmployeeRepository;
import com.ems.employeemanagementsystem.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for EmployeeServiceImpl using JUnit 5 and Mockito.
 */
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeDto employeeDto;

    @BeforeEach
    void setUp() {
        employee = new Employee(
                1L,
                "Alex Rivera",
                "alex.rivera@example.com",
                "+1-555-0101",
                "Engineering",
                "Backend Engineer",
                new BigDecimal("85000.00"),
                LocalDate.of(2023, 5, 10)
        );

        employeeDto = new EmployeeDto(
                1L,
                "Alex Rivera",
                "alex.rivera@example.com",
                "+1-555-0101",
                "Engineering",
                "Backend Engineer",
                new BigDecimal("85000.00"),
                LocalDate.of(2023, 5, 10)
        );
    }

    @Test
    @DisplayName("Should create employee successfully when email is unique")
    void testCreateEmployee_Success() {
        when(employeeRepository.existsByEmail("alex.rivera@example.com")).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee created = employeeService.createEmployee(employeeDto);

        assertThat(created).isNotNull();
        assertThat(created.getEmail()).isEqualTo("alex.rivera@example.com");
        assertThat(created.getFullName()).isEqualTo("Alex Rivera");
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should throw DuplicateEmailException when creating employee with existing email")
    void testCreateEmployee_DuplicateEmail_ThrowsException() {
        when(employeeRepository.existsByEmail("alex.rivera@example.com")).thenReturn(true);

        assertThatThrownBy(() -> employeeService.createEmployee(employeeDto))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessageContaining("already exists");

        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should retrieve employee by ID successfully")
    void testGetEmployeeById_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Employee found = employeeService.getEmployeeById(1L);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1L);
        assertThat(found.getFullName()).isEqualTo("Alex Rivera");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when employee ID does not exist")
    void testGetEmployeeById_NotFound_ThrowsException() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getEmployeeById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Employee not found with ID: 99");
    }

    @Test
    @DisplayName("Should update employee information successfully")
    void testUpdateEmployee_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.existsByEmailAndIdNot("alex.rivera@example.com", 1L)).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        employeeDto.setJobTitle("Staff Software Engineer");
        employeeDto.setSalary(new BigDecimal("110000.00"));

        Employee updated = employeeService.updateEmployee(1L, employeeDto);

        assertThat(updated).isNotNull();
        verify(employeeRepository).save(employee);
    }

    @Test
    @DisplayName("Should throw DuplicateEmailException when updating to an email already held by another employee")
    void testUpdateEmployee_DuplicateEmail_ThrowsException() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.existsByEmailAndIdNot("alex.rivera@example.com", 1L)).thenReturn(true);

        assertThatThrownBy(() -> employeeService.updateEmployee(1L, employeeDto))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessageContaining("already exists");

        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existent employee")
    void testUpdateEmployee_NotFound_ThrowsException() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.updateEmployee(99L, employeeDto))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Should delete employee successfully")
    void testDeleteEmployee_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(1L);

        verify(employeeRepository).delete(employee);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent employee")
    void testDeleteEmployee_NotFound_ThrowsException() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.deleteEmployee(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(employeeRepository, never()).delete(any(Employee.class));
    }

    @Test
    @DisplayName("Should compute department statistics accurately")
    void testGetDepartmentStatistics() {
        List<Object[]> rawStats = List.of(
                new Object[]{"Engineering", 4L},
                new Object[]{"Human Resources", 2L}
        );
        when(employeeRepository.countEmployeesByDepartment()).thenReturn(rawStats);

        Map<String, Long> stats = employeeService.getDepartmentStatistics();

        assertThat(stats).hasSize(2);
        assertThat(stats.get("Engineering")).isEqualTo(4L);
        assertThat(stats.get("Human Resources")).isEqualTo(2L);
    }

    @Test
    @DisplayName("Should calculate average salary correctly")
    void testGetAverageSalary() {
        when(employeeRepository.count()).thenReturn(2L);
        Employee emp2 = new Employee(2L, "Jane Doe", "jane@example.com", "555-0202",
                "Finance", "Analyst", new BigDecimal("75000.00"), LocalDate.now());
        when(employeeRepository.findAll()).thenReturn(List.of(employee, emp2));

        BigDecimal average = employeeService.getAverageSalary();

        // (85000 + 75000) / 2 = 80000.00
        assertThat(average).isEqualByComparingTo(new BigDecimal("80000.00"));
    }
}
