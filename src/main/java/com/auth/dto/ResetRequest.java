package com.auth.dto;

public class ResetRequest {
    private String email;
    private String newPassword;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setPassword(String password) {
        this.newPassword = password;
    }
}
