package com.ems.employeemanagementsystem.controller;

import com.ems.employeemanagementsystem.dto.EmployeeDto;
import com.ems.employeemanagementsystem.entity.Employee;
import com.ems.employeemanagementsystem.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Controller tests using Spring WebMvcTest and MockMvc.
 */
@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Test
    @DisplayName("GET /employees should render list view with model attributes")
    void testListEmployees_Success() throws Exception {
        Employee emp = new Employee(1L, "John Doe", "john@example.com", "555-0101",
                "Engineering", "Software Developer", new BigDecimal("80000.00"), LocalDate.now());
        when(employeeService.searchAndFilter(null, null)).thenReturn(List.of(emp));
        when(employeeService.getAllDepartments()).thenReturn(List.of("Engineering", "Finance"));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(view().name("employees/list"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attributeExists("departments"))
                .andExpect(model().attribute("totalFound", 1));
    }

    @Test
    @DisplayName("GET /employees/new should show create form")
    void testShowCreateForm() throws Exception {
        when(employeeService.getAllDepartments()).thenReturn(List.of("Engineering", "Human Resources"));

        mockMvc.perform(get("/employees/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("employees/form"))
                .andExpect(model().attributeExists("employeeDto"))
                .andExpect(model().attribute("isEdit", false));
    }

    @Test
    @DisplayName("POST /employees with valid data should redirect to /employees with flash success")
    void testCreateEmployee_Success() throws Exception {
        Employee created = new Employee(1L, "Alice Smith", "alice@example.com", "+1-555-0199",
                "Engineering", "QA Engineer", new BigDecimal("75000.00"), LocalDate.of(2023, 1, 1));

        when(employeeService.isEmailTaken("alice@example.com", null)).thenReturn(false);
        when(employeeService.createEmployee(any(EmployeeDto.class))).thenReturn(created);

        mockMvc.perform(post("/employees")
                        .param("fullName", "Alice Smith")
                        .param("email", "alice@example.com")
                        .param("phone", "+1-555-0199")
                        .param("department", "Engineering")
                        .param("jobTitle", "QA Engineer")
                        .param("salary", "75000.00")
                        .param("joiningDate", "2023-01-01"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    @DisplayName("POST /employees with invalid data should return form view with errors")
    void testCreateEmployee_ValidationError() throws Exception {
        when(employeeService.getAllDepartments()).thenReturn(List.of("Engineering"));

        mockMvc.perform(post("/employees")
                        .param("fullName", "") // Invalid: blank
                        .param("email", "invalid-email") // Invalid email
                        .param("salary", "-500")) // Invalid: negative
                .andExpect(status().isOk())
                .andExpect(view().name("employees/form"))
                .andExpect(model().hasErrors());
    }

    @Test
    @DisplayName("GET /employees/{id} should display employee details")
    void testViewEmployeeDetails() throws Exception {
        Employee emp = new Employee(1L, "John Doe", "john@example.com", "555-0101",
                "Engineering", "Dev", new BigDecimal("80000.00"), LocalDate.now());
        when(employeeService.getEmployeeById(1L)).thenReturn(emp);

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("employees/view"))
                .andExpect(model().attribute("employee", emp));
    }

    @Test
    @DisplayName("POST /employees/delete/{id} should delete and redirect")
    void testDeleteEmployee_Success() throws Exception {
        Employee emp = new Employee(1L, "John Doe", "john@example.com", "555-0101",
                "Engineering", "Dev", new BigDecimal("80000.00"), LocalDate.now());
        when(employeeService.getEmployeeById(1L)).thenReturn(emp);

        mockMvc.perform(post("/employees/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(employeeService).deleteEmployee(1L);
    }

    @Test
    @DisplayName("GET /employees/api/check-email should return JSON status")
    void testCheckEmailApi() throws Exception {
        when(employeeService.isEmailTaken("test@example.com", null)).thenReturn(true);

        mockMvc.perform(get("/employees/api/check-email").param("email", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taken").value(true));
    }
}
