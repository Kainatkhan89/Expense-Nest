package com.example.expensenest.entity;

public class UserSignIn {
    private String email;
    private String password;

    public UserSignIn(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
