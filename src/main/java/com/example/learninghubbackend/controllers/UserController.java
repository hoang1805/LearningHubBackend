package com.example.learninghubbackend.controllers;

import com.example.learninghubbackend.commons.exceptions.NotFoundException;
import com.example.learninghubbackend.dtos.responses.BaseResponse;
import com.example.learninghubbackend.models.User;
import com.example.learninghubbackend.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            throw new NotFoundException("User not found");
        }

        User user = userService.query().getUser(id);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.success(user.release())
        );
    }
}
