package com.example.learninghubbackend.services.user;

import com.example.learninghubbackend.commons.exceptions.NotFoundException;
import com.example.learninghubbackend.dtos.requests.user.ChangeInformation;
import com.example.learninghubbackend.dtos.requests.user.ChangePasswordRequest;
import com.example.learninghubbackend.dtos.requests.user.RegisterRequest;
import com.example.learninghubbackend.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserReader userReader;
    private final UserQuery userQuery;

    public UserReader reader() {
        return userReader;
    }

    public UserQuery query() {
        return userQuery;
    }

    public User createUser(RegisterRequest request) {
        User user = new User();
        userReader.readUser(user, request);
        return userQuery.save(user);
    }

    public void changePassword(User user, ChangePasswordRequest request) {
        userReader.readChangePassword(user, request);
        userQuery.save(user);
    }

    public User changePassword(Long userId, ChangePasswordRequest request) {
        User user = userQuery.getUser(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        changePassword(user, request);
        return user;
    }

    public void changeInformation(User user, ChangeInformation request) {
        userReader.readInformation(user, request);
        userQuery.save(user);
    }

    public User changeInformation(Long userId, ChangeInformation request) {
        User user = userQuery.getUser(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        changeInformation(user, request);
        return user;
    }
}
