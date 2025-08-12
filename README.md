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
     Endpoint: POST http://localhost:8080/api/auth/signup
Request Body

      {
         "firstName": "John",
         "lastName": "Doe",
         "email": "john@example.com",
         "username": "johndoe",
         "password": "password123"
      }

Response

      {
         "message": "Check your email for OTP verification",
         "email": "john@example.com"
      }


### 2. Email verification
     Endpoint: POST http://localhost:8080/api/auth/verify
Request Body

      {
         "email": "john@example.com",
         "otp": "6 digits pin here"
      }

Response

      {
         "message": "OTP verified successfully"
      }


### 3. login
     Endpoint: POST http://localhost:8080/api/auth/login
Request Body

      {
         "email_or_username": "john@example.com", //You can also login with your username
         "password":"password123"
      }

Response

      {
         "token": "your.jwt.token",
         "message": "Login successful"
      }


### 4. Get User Details (Authenticated)
      Endpoint: GET http://localhost:8080/api/user/profile
Headers

      Authorization: Bearer your.jwt.token


Response

      {
         "firstName": "John",
         "lastName": "Doe",
         "email": "johndoe@example.com",
         "username": "johndoe"
      }
      

### 5. Forgot Password
     Endpoint: POST http://localhost:8080/api/auth/forgot-password
Request Body

      {
         "email": "johndoe@example.com"
      }

Response

      {
         "message": "Check your email for OTP verification",
         "email": "john@example.com"
      }

    

### 6. Verification of Reset OTP
     Endpoint: POST http://localhost:8080/api/auth/verify-reset-otp
Request Body

      {
         "email": "john@example.com",
         "otp": "6 digits pin here"
      }

Response

      {
         "message": "OTP verified successfully, you can now reset your password"
      }


    

### 7. Reset Password
     Endpoint: POST http://localhost:8080/api/auth/reset-password
Request Body

      {
         "email": "john@example.com",
         "password": "NewPassword"
      }

Response

      {
         "message": "Password reset successfully"
      }

