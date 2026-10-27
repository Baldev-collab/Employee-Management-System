package com.ems.employeemanagementsystem.controller;

import com.ems.employeemanagementsystem.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller responsible for the analytical Dashboard view.
 * Computes business metrics, department distributions, and recent hires.
 */
@Controller
public class DashboardController {

    private final EmployeeService employeeService;

    public DashboardController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping({"/", "/dashboard"})
    public String showDashboard(Model model) {
        model.addAttribute("activePage", "dashboard");
        model.addAttribute("totalEmployees", employeeService.getTotalEmployees());
        model.addAttribute("totalDepartments", employeeService.getTotalDepartmentsCount());
        model.addAttribute("totalPayroll", employeeService.getTotalPayroll());
        model.addAttribute("averageSalary", employeeService.getAverageSalary());
        model.addAttribute("departmentStats", employeeService.getDepartmentStatistics());
        model.addAttribute("recentHires", employeeService.getRecentHires());
        return "dashboard";
    }
}
