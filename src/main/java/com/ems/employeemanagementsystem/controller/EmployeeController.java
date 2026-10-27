package com.ems.employeemanagementsystem.controller;

import com.ems.employeemanagementsystem.dto.EmployeeDto;
import com.ems.employeemanagementsystem.entity.Employee;
import com.ems.employeemanagementsystem.exception.DuplicateEmailException;
import com.ems.employeemanagementsystem.exception.ResourceNotFoundException;
import com.ems.employeemanagementsystem.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

/**
 * Controller handling Employee CRUD, search, filter, and detail views.
 */
@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Display all employees with search and department filtering.
     */
    @GetMapping
    public String listEmployees(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "department", required = false) String department,
            Model model) {

        List<Employee> employees = employeeService.searchAndFilter(keyword, department);
        List<String> departments = employeeService.getAllDepartments();

        model.addAttribute("activePage", "employees");
        model.addAttribute("employees", employees);
        model.addAttribute("departments", departments);
        model.addAttribute("keyword", keyword != null ? keyword : "");
        model.addAttribute("selectedDepartment", department != null ? department : "All");
        model.addAttribute("totalFound", employees.size());

        return "employees/list";
    }

    /**
     * Display form to create a new employee.
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("activePage", "add-employee");
        model.addAttribute("employeeDto", new EmployeeDto());
        model.addAttribute("departments", employeeService.getAllDepartments());
        model.addAttribute("isEdit", false);
        model.addAttribute("pageTitle", "Register New Employee");
        return "employees/form";
    }

    /**
     * Process creation of a new employee with server-side validation.
     */
    @PostMapping
    public String createEmployee(
            @Valid @ModelAttribute("employeeDto") EmployeeDto employeeDto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Check for duplicate email before saving
        if (!bindingResult.hasFieldErrors("email") &&
                employeeService.isEmailTaken(employeeDto.getEmail(), null)) {
            bindingResult.rejectValue("email", "error.duplicate", "This email address is already registered to another employee.");
        }

        // If validation errors are present, re-render form with field error messages
        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "add-employee");
            model.addAttribute("departments", employeeService.getAllDepartments());
            model.addAttribute("isEdit", false);
            model.addAttribute("pageTitle", "Register New Employee");
            return "employees/form";
        }

        try {
            Employee created = employeeService.createEmployee(employeeDto);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Employee '" + created.getFullName() + "' (ID: #" + created.getId() + ") was successfully created!"
            );
            return "redirect:/employees";
        } catch (DuplicateEmailException ex) {
            bindingResult.rejectValue("email", "error.duplicate", ex.getMessage());
            model.addAttribute("activePage", "add-employee");
            model.addAttribute("departments", employeeService.getAllDepartments());
            model.addAttribute("isEdit", false);
            model.addAttribute("pageTitle", "Register New Employee");
            return "employees/form";
        }
    }

    /**
     * View detailed profile of an individual employee.
     */
    @GetMapping("/{id}")
    public String viewEmployeeDetails(@PathVariable("id") Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        model.addAttribute("activePage", "employees");
        model.addAttribute("employee", employee);
        return "employees/view";
    }

    /**
     * Display form to edit an existing employee.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        EmployeeDto dto = employeeService.convertToDto(employee);

        model.addAttribute("activePage", "employees");
        model.addAttribute("employeeDto", dto);
        model.addAttribute("employeeId", id);
        model.addAttribute("departments", employeeService.getAllDepartments());
        model.addAttribute("isEdit", true);
        model.addAttribute("pageTitle", "Edit Employee #" + id + " - " + employee.getFullName());
        return "employees/form";
    }

    /**
     * Process updates to an existing employee.
     */
    @PostMapping("/update/{id}")
    public String updateEmployee(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("employeeDto") EmployeeDto employeeDto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Check for duplicate email excluding current employee
        if (!bindingResult.hasFieldErrors("email") &&
                employeeService.isEmailTaken(employeeDto.getEmail(), id)) {
            bindingResult.rejectValue("email", "error.duplicate", "This email address is already in use by another employee.");
        }

        if (bindingResult.hasErrors()) {
            employeeDto.setId(id);
            model.addAttribute("activePage", "employees");
            model.addAttribute("employeeId", id);
            model.addAttribute("departments", employeeService.getAllDepartments());
            model.addAttribute("isEdit", true);
            model.addAttribute("pageTitle", "Edit Employee #" + id);
            return "employees/form";
        }

        try {
            Employee updated = employeeService.updateEmployee(id, employeeDto);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Employee details for '" + updated.getFullName() + "' were updated successfully!"
            );
            return "redirect:/employees";
        } catch (DuplicateEmailException ex) {
            bindingResult.rejectValue("email", "error.duplicate", ex.getMessage());
            employeeDto.setId(id);
            model.addAttribute("activePage", "employees");
            model.addAttribute("employeeId", id);
            model.addAttribute("departments", employeeService.getAllDepartments());
            model.addAttribute("isEdit", true);
            model.addAttribute("pageTitle", "Edit Employee #" + id);
            return "employees/form";
        }
    }

    /**
     * Delete an employee record.
     */
    @PostMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            Employee employee = employeeService.getEmployeeById(id);
            employeeService.deleteEmployee(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Employee '" + employee.getFullName() + "' (ID: #" + id + ") has been permanently deleted."
            );
        } catch (ResourceNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/employees";
    }

    /**
     * Real-time AJAX endpoint to verify email uniqueness on the client side.
     */
    @GetMapping("/api/check-email")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkEmailUniqueness(
            @RequestParam("email") String email,
            @RequestParam(name = "excludeId", required = false) Long excludeId) {
        boolean isTaken = employeeService.isEmailTaken(email, excludeId);
        return ResponseEntity.ok(Map.of("taken", isTaken));
    }
}
