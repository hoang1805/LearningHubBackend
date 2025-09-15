package com.example.learninghubbackend.dtos.requests.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LoginRequest {
    public String username;
    public String password;
}
