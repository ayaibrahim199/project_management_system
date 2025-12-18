# Project Management System

## Overview

A lightweight web application for managing projects, tasks, teams, and attachments. Developed using **Spring Boot** .

### Key Features:
- Core: Java 25, Spring Boot, Thymeleaf templates, PostgreSQL database
- Attachments: Upload and download support

---

## Getting Started

### Prerequisites:

Make sure you have these installed:
1. Java 25 SDK
2. PostgreSQL (set up credentials in `application.properties`)
3. Maven (`./mvnw` wrapper included)

### Steps to Run:

1. **Set up PostgreSQL**  
   - Create a database named `project_management`.  
   - Update `application.properties` with database credentials if required.

2. **Build and Run Backend**  
   - Build the project (skip tests for faster setup):  
     ```bash
     ./mvnw -DskipTests package
     ```  
   - Start the backend server:  
     ```bash
     ./mvnw spring-boot:run
     ```  
   - The app will be accessible at: `http://localhost:8080/login`.


---

## Useful Commands

- Run Tests:  
  ```bash
  ./mvnw test
  ```  
- Start Backend:  
  ```bash
  ./mvnw spring-boot:run
  ```  
- Build Application:  
  ```bash
  ./mvnw -DskipTests package
  ```

---

## Configuration

All application settings (e.g., database credentials) are managed in:  
`src/main/resources/application.properties`

For production, use environment variables:
- `SPRING_DATASOURCE_URL`  
- `SPRING_DATASOURCE_USERNAME`  
- `SPRING_DATASOURCE_PASSWORD`

---

## Troubleshooting

### Common Issues:
1. **Port Already in Use**  
   - Identify and stop the process:  
     ```bash
     lsof -i :8080
     kill <pid>
     ```  

2. **Database Connection Errors**  
   - Ensure correct database credentials in `application.properties`.  
   - Confirm that PostgreSQL is running and accessible.

3. **Authentication Issues**  
   - Check the security settings in `SecurityConfig`.

---




