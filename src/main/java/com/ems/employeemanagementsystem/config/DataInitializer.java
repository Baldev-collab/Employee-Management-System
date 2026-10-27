package com.ems.employeemanagementsystem.config;

import com.ems.employeemanagementsystem.entity.Employee;
import com.ems.employeemanagementsystem.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Seeds initial demo data if the employee database table is empty.
 * Ensures the system is ready for immediate demonstration and evaluation.
 */
@Configuration
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private final EmployeeRepository employeeRepository;

    public DataInitializer(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(String... args) {
        if (employeeRepository.count() == 0) {
            log.info("Populating initial sample employees for demonstration...");

            List<Employee> sampleEmployees = List.of(
                    new Employee(null, "Sarah Jenkins", "sarah.jenkins@acmecorp.com", "+1-555-0142",
                            "Engineering", "Principal Software Architect", new BigDecimal("128500.00"),
                            LocalDate.of(2021, 3, 15)),
                    new Employee(null, "David Chen", "david.chen@acmecorp.com", "+1-555-0177",
                            "Engineering", "Senior Full-Stack Engineer", new BigDecimal("98000.00"),
                            LocalDate.of(2022, 6, 1)),
                    new Employee(null, "Elena Rostova", "elena.rostova@acmecorp.com", "+1-555-0199",
                            "Product Management", "Lead Product Manager", new BigDecimal("112000.00"),
                            LocalDate.of(2022, 11, 10)),
                    new Employee(null, "Marcus Washington", "marcus.w@acmecorp.com", "+1-555-0128",
                            "Human Resources", "HR Operations Director", new BigDecimal("86500.00"),
                            LocalDate.of(2020, 8, 20)),
                    new Employee(null, "Priya Patel", "priya.patel@acmecorp.com", "+1-555-0133",
                            "Finance", "Senior Financial Analyst", new BigDecimal("91000.00"),
                            LocalDate.of(2023, 1, 9)),
                    new Employee(null, "James O'Connor", "james.oc@acmecorp.com", "+1-555-0185",
                            "Marketing", "Digital Growth Strategist", new BigDecimal("74000.00"),
                            LocalDate.of(2023, 7, 18)),
                    new Employee(null, "Amina Al-Mansoor", "amina.mansoor@acmecorp.com", "+1-555-0164",
                            "Sales", "Enterprise Account Executive", new BigDecimal("105000.00"),
                            LocalDate.of(2024, 2, 5)),
                    new Employee(null, "Liam Gallagher", "liam.gallagher@acmecorp.com", "+1-555-0112",
                            "Operations", "DevOps & Cloud Engineer", new BigDecimal("95000.00"),
                            LocalDate.of(2024, 5, 14))
            );

            employeeRepository.saveAll(sampleEmployees);
            log.info("Successfully seeded {} sample employees.", sampleEmployees.size());
        }
    }
}
