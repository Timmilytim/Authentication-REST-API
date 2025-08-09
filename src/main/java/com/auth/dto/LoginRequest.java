package com.auth.dto;

public class LoginRequest {
    private String email_or_username;
    private String password;


    public String getEmail_or_username() {
        return email_or_username;
    }

    public void setEmail_or_username(String email_or_username) {
        this.email_or_username = email_or_username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
