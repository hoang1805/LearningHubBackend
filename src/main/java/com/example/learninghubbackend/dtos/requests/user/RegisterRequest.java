package com.example.learninghubbackend.dtos.requests.user;

import com.example.learninghubbackend.services.user.Gender;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RegisterRequest {
    public String username;

    public String email;

    public String phone;

    public String name;

    public Gender gender;

    public String password;

    public String confirmPassword;

    public String role;
}
