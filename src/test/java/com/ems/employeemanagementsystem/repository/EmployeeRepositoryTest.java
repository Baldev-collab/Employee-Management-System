package com.ems.employeemanagementsystem.repository;

import com.ems.employeemanagementsystem.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for Spring Data JPA EmployeeRepository using in-memory H2 database.
 */
@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();

        employee1 = new Employee(
                null,
                "Lucas Scott",
                "lucas.scott@example.com",
                "+1-555-1111",
                "Engineering",
                "Software Engineer",
                new BigDecimal("90000.00"),
                LocalDate.of(2022, 1, 15)
        );

        employee2 = new Employee(
                null,
                "Maya Lin",
                "maya.lin@example.com",
                "+1-555-2222",
                "Marketing",
                "Marketing Specialist",
                new BigDecimal("65000.00"),
                LocalDate.of(2023, 4, 10)
        );

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
    }

    @Test
    @DisplayName("Should find employee by unique email")
    void testFindByEmail() {
        Optional<Employee> found = employeeRepository.findByEmail("lucas.scott@example.com");

        assertThat(found).isPresent();
        assertThat(found.get().getFullName()).isEqualTo("Lucas Scott");
    }

    @Test
    @DisplayName("Should return true when email exists")
    void testExistsByEmail() {
        boolean exists = employeeRepository.existsByEmail("maya.lin@example.com");
        boolean notExists = employeeRepository.existsByEmail("unknown@example.com");

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("Should detect email collision with other employee IDs")
    void testExistsByEmailAndIdNot() {
        Long emp1Id = employee1.getId();
        Long emp2Id = employee2.getId();

        // emp1 testing with its own email -> false (it is allowed to keep its own email)
        boolean sameEmployee = employeeRepository.existsByEmailAndIdNot("lucas.scott@example.com", emp1Id);
        // emp2 attempting to take emp1's email -> true (collision!)
        boolean differentEmployee = employeeRepository.existsByEmailAndIdNot("lucas.scott@example.com", emp2Id);

        assertThat(sameEmployee).isFalse();
        assertThat(differentEmployee).isTrue();
    }

    @Test
    @DisplayName("Should filter employees by department")
    void testFindByDepartment() {
        List<Employee> engList = employeeRepository.findByDepartmentIgnoreCase("Engineering");
        List<Employee> mktList = employeeRepository.findByDepartmentIgnoreCase("Marketing");

        assertThat(engList).hasSize(1);
        assertThat(engList.get(0).getFullName()).isEqualTo("Lucas Scott");
        assertThat(mktList).hasSize(1);
        assertThat(mktList.get(0).getFullName()).isEqualTo("Maya Lin");
    }

    @Test
    @DisplayName("Should search employees by name or email keyword")
    void testSearchByNameOrEmail() {
        List<Employee> results = employeeRepository.searchByNameOrEmail("lucas");
        List<Employee> emailResults = employeeRepository.searchByNameOrEmail("maya.lin");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getFullName()).isEqualTo("Lucas Scott");
        assertThat(emailResults).hasSize(1);
        assertThat(emailResults.get(0).getEmail()).isEqualTo("maya.lin@example.com");
    }

    @Test
    @DisplayName("Should filter by both keyword and department")
    void testSearchAndFilter() {
        List<Employee> results = employeeRepository.searchAndFilter("Lucas", "Engineering");
        List<Employee> emptyResults = employeeRepository.searchAndFilter("Lucas", "Marketing");

        assertThat(results).hasSize(1);
        assertThat(emptyResults).isEmpty();
    }
}
