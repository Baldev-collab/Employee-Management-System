-- ===================================================================
-- Employee Management System - Database Initialization Script
-- Database: employee_management
-- ===================================================================

CREATE DATABASE IF NOT EXISTS employee_management
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE employee_management;

-- Create Employees Table
CREATE TABLE IF NOT EXISTS employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    department VARCHAR(50) NOT NULL,
    job_title VARCHAR(100) NOT NULL,
    salary DECIMAL(12, 2) NOT NULL,
    joining_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_department (department),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Sample Seed Data
INSERT IGNORE INTO employees (full_name, email, phone, department, job_title, salary, joining_date, created_at, updated_at)
VALUES
('Sarah Jenkins', 'sarah.jenkins@acmecorp.com', '+1-555-0142', 'Engineering', 'Principal Software Architect', 128500.00, '2021-03-15', NOW(), NOW()),
('David Chen', 'david.chen@acmecorp.com', '+1-555-0177', 'Engineering', 'Senior Full-Stack Engineer', 98000.00, '2022-06-01', NOW(), NOW()),
('Elena Rostova', 'elena.rostova@acmecorp.com', '+1-555-0199', 'Product Management', 'Lead Product Manager', 112000.00, '2022-11-10', NOW(), NOW()),
('Marcus Washington', 'marcus.w@acmecorp.com', '+1-555-0128', 'Human Resources', 'HR Operations Director', 86500.00, '2020-08-20', NOW(), NOW()),
('Priya Patel', 'priya.patel@acmecorp.com', '+1-555-0133', 'Finance', 'Senior Financial Analyst', 91000.00, '2023-01-09', NOW(), NOW()),
('James O\'Connor', 'james.oc@acmecorp.com', '+1-555-0185', 'Marketing', 'Digital Growth Strategist', 74000.00, '2023-07-18', NOW(), NOW()),
('Amina Al-Mansoor', 'amina.mansoor@acmecorp.com', '+1-555-0164', 'Sales', 'Enterprise Account Executive', 105000.00, '2024-02-05', NOW(), NOW()),
('Liam Gallagher', 'liam.gallagher@acmecorp.com', '+1-555-0112', 'Operations', 'DevOps & Cloud Engineer', 95000.00, '2024-05-14', NOW(), NOW());
