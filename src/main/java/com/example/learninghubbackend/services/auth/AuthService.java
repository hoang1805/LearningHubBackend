package com.example.learninghubbackend.services.auth;

import com.example.learninghubbackend.dtos.requests.auth.LoginRequest;
import com.example.learninghubbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void login(LoginRequest loginRequest) {}

    public void logout(String userId) {}
}
