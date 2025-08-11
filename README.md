# User Authentication API (Spring Boot + JWT)

A simple REST API for user authentication and profile management, built with **Spring Boot** and **JWT**.  This API provides authentication services for our application, including user registration, login, OTP verification, password reset, and related endpoints.

Base URL: https://localhost:8080/api (Still local)

---

## 🚀 Features
- **JWT-based authentication** for secure API calls
- **Role-based endpoint protection** (optional)
- **Spring Security** integration
- **RESTful API** design

---

## 🛠️ Local Setup

### Prerequisites
- **Java 17+**
- **Maven** or **Gradle**
- **MySQL** (or update `application.yml` for another DB)
- IDE (IntelliJ, Eclipse, VS Code)

---

### Steps to Run Locally
1. **Clone Repository**
   ```bash
   git clone https://github.com/Timmilytim/Authentication-REST-API.git
   cd Authentication-REST-API

2. **Open IDE**

3. **Configure Database & Mail Settings**
   Edit the **_src/main/resources/application.yml_** file with your DB and email credentials:
      ```bash
      server:
         port: 8080

      spring:
         datasource:
            url: jdbc:mysql://localhost:3306/<your_db_name>
            username: <db_username>
            password: <db_password>
            driver-class-name: com.mysql.cj.jdbc.Driver
         jpa:
            hibernate:
               ddl-auto: update
            show-sql: true
            properties:
               hibernate:
               format_sql: true
         mail:
            host: smtp.gmail.com
            port: 587
            username: <your_email>
            password: <your_gmail_app_password>

4. **Run Application**
   ```bash
      mvn spring-boot:run
---

## 📡 API Endpoints
### 1. Sign Up
      http://localhost:8080/api/auth/signup
Request Body

      {
         "firstName": "John",
         "lastName": "Doe",
         "email": "john@example.com",
         "username": "johndoe",
         "password": "password123"
      }

Response
  ```bash
     {
      "message": "Check your email for OTP verification",
      "email": "john@example.com"
      }
