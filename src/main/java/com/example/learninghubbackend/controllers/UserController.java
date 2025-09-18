package com.example.learninghubbackend.controllers;

import com.example.learninghubbackend.commons.exceptions.NotFoundException;
import com.example.learninghubbackend.dtos.requests.user.ChangeInformation;
import com.example.learninghubbackend.dtos.requests.user.ChangePasswordRequest;
import com.example.learninghubbackend.dtos.responses.BaseResponse;
import com.example.learninghubbackend.models.User;
import com.example.learninghubbackend.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/user/v1")
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

    @PutMapping("/change/password")
    public ResponseEntity<Object> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.query().getUser(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        userService.changePassword(user, changePasswordRequest);

        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.success()
        );
    }

    @PutMapping("/change/information")
    public ResponseEntity<Object> changeInformation(@RequestBody ChangeInformation changeInformation) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.query().getUser(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        userService.changeInformation(user, changeInformation);
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.success(user.release())
        );
    }
}
