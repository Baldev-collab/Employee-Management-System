package com.ems.employeemanagementsystem.repository;

import com.ems.employeemanagementsystem.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for Employee persistence operations.
 * Provides out-of-the-box CRUD methods plus custom query derivation and JPQL queries.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Check if an employee exists with the given email.
     */
    boolean existsByEmail(String email);

    /**
     * Check if any other employee (different ID) has this email.
     * Essential for validating employee update operations.
     */
    boolean existsByEmailAndIdNot(String email, Long id);

    /**
     * Find an employee by unique email.
     */
    Optional<Employee> findByEmail(String email);

    /**
     * Find employees by department (case-insensitive).
     */
    List<Employee> findByDepartmentIgnoreCase(String department);

    /**
     * Search employees by name or email containing keyword (case-insensitive).
     */
    @Query("SELECT e FROM Employee e WHERE " +
           "LOWER(e.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "ORDER BY e.id DESC")
    List<Employee> searchByNameOrEmail(@Param("keyword") String keyword);

    /**
     * Combined search and department filter query.
     */
    @Query("SELECT e FROM Employee e WHERE " +
           "(:department IS NULL OR :department = '' OR LOWER(e.department) = LOWER(:department)) AND " +
           "(:keyword IS NULL OR :keyword = '' OR LOWER(e.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY e.id DESC")
    List<Employee> searchAndFilter(@Param("keyword") String keyword, @Param("department") String department);

    /**
     * Group count of employees per department for dashboard statistics.
     */
    @Query("SELECT e.department, COUNT(e) FROM Employee e GROUP BY e.department ORDER BY COUNT(e) DESC")
    List<Object[]> countEmployeesByDepartment();

    /**
     * Retrieve the 5 most recently joined employees for dashboard feed.
     */
    List<Employee> findTop5ByOrderByJoiningDateDesc();

    /**
     * Retrieve list of all distinct department names.
     */
    @Query("SELECT DISTINCT e.department FROM Employee e ORDER BY e.department ASC")
    List<String> findDistinctDepartments();
}
