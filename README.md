# Employee Management System (EMS)

A full-stack, enterprise-grade Employee Management System built from scratch using **Java 21**, **Spring Boot 3**, **Spring Data JPA / Hibernate**, **Thymeleaf**, **Bootstrap 5**, and **MySQL**.

Developed for college project demonstration and evaluation, adhering to industry standard layered architecture, clean code practices, server & client validation, and automated testing.

---

## 🌟 Key Features

1. **Analytical Executive Dashboard**:
   - Total employee headcount, active departments count, annual payroll expenditure, and average salary calculations.
   - Visual department staff distribution progress bars.
   - Real-time recent hires feed with employee profile links.
2. **Employee Directory (Table View)**:
   - Full list of employees with visual avatar initials, designation, contact info, and color-coded department tags.
   - Formatted currency displays and localized join dates.
3. **Advanced Search & Filtering**:
   - Search by full name or email (case-insensitive substring match).
   - Filter by department dropdown.
   - Search and department filters work simultaneously.
4. **Interactive Create & Edit**:
   - Client-side validation (HTML5 + Bootstrap 5 validation states).
   - Server-side Bean Validation (`@NotBlank`, `@Email`, `@Pattern`, `@DecimalMin`, `@NotNull`, `@PastOrPresent`).
   - Asynchronous real-time email uniqueness check via AJAX (`/employees/api/check-email`).
   - Pre-populated edit forms with collision protection (allows employees to keep their own email while preventing duplicates).
5. **Safe Deletion with Confirmation Dialog**:
   - Interactive Bootstrap confirmation modal showing employee name and ID before deletion.
6. **Individual Employee Profile View**:
   - Hero header with status badge and avatar.
   - 4-card breakdown: Contact details, Department & Role, Compensation & Payroll calculations (Annual + Monthly breakdown), and Audit metadata (`created_at` and `updated_at`).
7. **Robust Error Handling & Empty States**:
   - Global exception handling (`@ControllerAdvice`) rendering clean HTTP 404, 409, and 500 error pages.
   - User-friendly empty states for zero search results.
8. **Automated Testing Suite**:
   - 24 automated unit and integration tests using **JUnit 5**, **Mockito**, **MockMvc**, and **@DataJpaTest** on an in-memory H2 database.

---

## 🛠️ Technology Stack

| Layer | Technology |
|---|---|
| **Language** | Java 21 LTS |
| **Backend Framework** | Spring Boot 3.3.5 (Spring MVC, Spring Data JPA, Hibernate, Validation) |
| **Database** | MySQL Community Server 8.x / 8.4 LTS (In-memory H2 for tests & demo mode) |
| **Templating Engine** | Thymeleaf 3.1 |
| **Frontend UI** | HTML5, CSS3, JavaScript (Vanilla ES6+), Bootstrap 5.3, Bootstrap Icons 1.11 |
| **Typography** | Inter (Google Fonts) |
| **Build & Dependency Management** | Apache Maven 3.10.0 |
| **Automated Testing** | JUnit 5, Mockito 5.11, AssertJ, Spring Test (MockMvc) |

---

## 📂 Project Structure

```text
Devops/
├── pom.xml                               # Maven project configuration & dependencies
├── .gitignore                            # Git exclusion rules
├── .env.example                          # Environment configuration template
├── README.md                             # Comprehensive project documentation
├── sql/
│   └── schema.sql                        # MySQL database & table creation script
└── src/
    ├── main/
    │   ├── java/com/ems/employeemanagementsystem/
    │   │   ├── EmployeeManagementSystemApplication.java  # Main Spring Boot launcher
    │   │   ├── config/
    │   │   │   └── DataInitializer.java                 # Seeds initial demo data if DB is empty
    │   │   ├── controller/
    │   │   │   ├── DashboardController.java             # Dashboard metrics & analytics
    │   │   │   ├── EmployeeController.java              # CRUD, search, filter, and AJAX endpoints
    │   │   │   └── GlobalExceptionHandler.java          # Centralized exception handling
    │   │   ├── dto/
    │   │   │   └── EmployeeDto.java                     # Form binding and server-side validation rules
    │   │   ├── entity/
    │   │   │   └── Employee.java                        # JPA Entity mapped to 'employees' table
    │   │   ├── exception/
    │   │   │   ├── DuplicateEmailException.java         # 409 Conflict business exception
    │   │   │   └── ResourceNotFoundException.java       # 404 Not Found business exception
    │   │   ├── repository/
    │   │   │   └── EmployeeRepository.java              # Spring Data JPA queries & JPQL
    │   │   └── service/
    │   │       ├── EmployeeService.java                 # Service interface
    │   │       └── impl/
    │   │           └── EmployeeServiceImpl.java         # Business logic & transactional operations
    │   └── resources/
    │       ├── application.properties                   # Main configuration (MySQL)
    │       ├── application-demo.properties              # Zero-setup offline/demo configuration (H2)
    │       ├── static/
    │       │   ├── css/
    │       │   │   └── style.css                        # Modern custom styling & dashboard theme
    │       │   └── js/
    │       │       └── main.js                          # Real-time email check, delete modal, & UI logic
    │       └── templates/
    │           ├── dashboard.html                       # Analytics dashboard view
    │           ├── error/
    │           │   └── error.html                       # Global error template
    │           ├── fragments/
    │           │   └── layout.html                      # Reusable sidebar, topbar, alerts, and footer
    │           └── employees/
    │               ├── list.html                        # Employee directory table with filters
    │               ├── form.html                        # Add / Edit employee form
    │               └── view.html                        # Comprehensive employee profile
    └── test/
        ├── java/com/ems/employeemanagementsystem/
        │   ├── controller/
        │   │   └── EmployeeControllerTest.java          # MockMvc web layer tests
        │   ├── repository/
        │   │   └── EmployeeRepositoryTest.java          # DataJpaTest database query tests
        │   └── service/
        │       └── EmployeeServiceTest.java             # Mockito unit tests for business logic
        └── resources/
            └── application.properties                   # In-memory test database properties
```

---

## 🚀 Quick Start Guide

### Option A: Run Instantly in Demo Mode (Zero Setup Required)
You can launch and test the full application immediately in your browser using the built-in embedded database mode (no MySQL installation required):

```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=demo"
```

Open your browser to:
👉 **`http://localhost:8088/`**

---

### Option B: Run with MySQL Community Server (Recommended for Submission)

#### Step 1: Install MySQL Community Server on Windows 11
If you do not have MySQL installed on your Windows 11 machine yet:

1. **Download the Official MySQL Installer**:
   - Visit: [https://dev.mysql.com/downloads/installer/](https://dev.mysql.com/downloads/installer/)
   - Download the **MySQL Installer Community** MSI (`mysql-installer-community-8.0.x.msi` or `8.4.x LTS`).
2. **Run the Installer**:
   - Choose Setup Type: **Developer Default** or **Server only**.
   - Proceed through the installation wizard.
3. **Configure MySQL Server**:
   - **Type and Networking**: Leave Default Port as `3306`.
   - **Authentication Method**: Choose **Use Strong Password Encryption** (recommended).
   - **Accounts and Roles**: Set a **MySQL Root Password** (e.g. `root` or `admin123`). **Remember this password!**
   - **Windows Service**: Keep "Start the MySQL Server at System Startup" checked (Service Name: `MySQL80` or `MySQL84`).
   - Click **Execute** to apply configuration and complete installation.

#### Step 2: Verify MySQL Service is Running
Open PowerShell as Administrator and run:
```powershell
Get-Service -Name *mysql*
```
You should see status `Running`. If stopped, start it:
```powershell
Start-Service -Name MySQL80
```

#### Step 3: Create the Database (Optional but Recommended)
Open the **MySQL 8.0 Command Line Client** from your Windows Start Menu, enter your root password, and execute:
```sql
CREATE DATABASE IF NOT EXISTS employee_management;
```
*(Note: Spring Boot's JDBC URL also contains `createDatabaseIfNotExist=true`, so MySQL Connector will automatically create the database schema if your user has permission!)*

#### Step 4: Configure Database Credentials
You can either edit `src/main/resources/application.properties` directly, or set environment variables:

**In PowerShell:**
```powershell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_mysql_password"
```

Or configure `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/employee_management?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_mysql_password
```

#### Step 5: Start the Spring Boot Application
```powershell
mvn spring-boot:run
```

Open your browser and navigate to:
👉 **`http://localhost:8088/`**

---

## 🧪 Running Automated Tests

To execute all 24 unit and integration tests across the Controller, Service, and Repository layers:

```powershell
mvn clean test
```

### Test Coverage Highlights:
- **EmployeeServiceTest**:
  - `testCreateEmployee_Success`
  - `testCreateEmployee_DuplicateEmail_ThrowsException`
  - `testGetEmployeeById_Success`
  - `testGetEmployeeById_NotFound_ThrowsException`
  - `testUpdateEmployee_Success`
  - `testUpdateEmployee_DuplicateEmail_ThrowsException`
  - `testUpdateEmployee_NotFound_ThrowsException`
  - `testDeleteEmployee_Success`
  - `testDeleteEmployee_NotFound_ThrowsException`
  - `testGetDepartmentStatistics`
  - `testGetAverageSalary`
- **EmployeeRepositoryTest**:
  - `testFindByEmail`
  - `testExistsByEmail`
  - `testExistsByEmailAndIdNot`
  - `testFindByDepartment`
  - `testSearchByNameOrEmail`
  - `testSearchAndFilter`
- **EmployeeControllerTest**:
  - `testListEmployees_Success`
  - `testShowCreateForm`
  - `testCreateEmployee_Success`
  - `testCreateEmployee_ValidationError`
  - `testViewEmployeeDetails`
  - `testDeleteEmployee_Success`
  - `testCheckEmailApi`

---

## 📦 Building Production Executable JAR

To package the complete self-contained executable JAR with all dependencies:

```powershell
mvn clean package
```

The resulting JAR file will be located at:
`target/employee-management-system-1.0.0.jar`

To run the JAR independently:
```powershell
java -jar target/employee-management-system-1.0.0.jar
```

---

## 💡 Important Architectural & Design Decisions

1. **Layered Architecture & Separation of Concerns**:
   - `Controller`: Handles HTTP routing, input binding, form validation checks, and view template selection.
   - `Service`: Encapsulates all transactional business logic (`@Transactional`), computations (payroll, average salary, statistics), and business rule enforcement.
   - `Repository`: Pure data access layer utilizing Spring Data JPA and Hibernate for automatic ORM query translation.
2. **DTO Pattern (Data Transfer Object)**:
   - Form inputs bind to `EmployeeDto` rather than the `Employee` entity directly.
   - Prevents Mass Assignment vulnerabilities, isolates validation rules to web interactions, and avoids tight coupling between view models and database entities.
3. **Constructor-Based Dependency Injection**:
   - All controllers and services utilize constructor injection rather than `@Autowired` field injection.
   - Guarantees immutability (`final` references), avoids hidden dependencies, and makes unit testing with Mockito clean and frictionless.
4. **Duplicate Email Prevention**:
   - Multi-tiered enforcement:
     1. Live AJAX check via JavaScript while the user types in the form.
     2. Server-side validation in `EmployeeController` and `EmployeeService` before saving.
     3. Database-level `UNIQUE` constraint on the `email` column in the `employees` table.
5. **Polished Enterprise UI**:
   - Built using Bootstrap 5.3 with custom modern CSS variables, soft shadows, rounded corners, responsive mobile drawer, and Google Fonts Inter.
   - No basic tutorial looks—designed as a real enterprise corporate portal.
