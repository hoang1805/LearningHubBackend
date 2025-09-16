package com.example.learninghubbackend.controllers;

import com.example.learninghubbackend.dtos.requests.auth.LoginRequest;
import com.example.learninghubbackend.dtos.requests.user.RegisterRequest;
import com.example.learninghubbackend.dtos.responses.BaseResponse;
import com.example.learninghubbackend.models.User;
import com.example.learninghubbackend.commons.ClientInfo;
import com.example.learninghubbackend.services.auth.AuthService;
import com.example.learninghubbackend.services.auth.session.SessionService;
import com.example.learninghubbackend.services.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest, HttpServletRequest servletRequest) {
        ClientInfo clientInfo = ClientInfo.getClientInfo(servletRequest);
        authService.login(loginRequest, clientInfo);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success());
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest request) {
        if (!request.password.equals(request.confirmPassword)) {
            throw new IllegalStateException("Passwords do not match");
        }

        User user = userService.reader().createUser(request);
        userService.query().save(user);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success());
    }
}
