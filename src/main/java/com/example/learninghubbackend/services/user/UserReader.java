package com.example.learninghubbackend.services.user;

import com.example.learninghubbackend.dtos.requests.user.RegisterRequest;
import com.example.learninghubbackend.models.User;
import com.example.learninghubbackend.utils.HashUtil;
import com.example.learninghubbackend.utils.ReaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReader {
    private final UserQuery userQuery;

    public User createUser(RegisterRequest request) {
        User user = new User();

        readUsername(user, request.username);
        readPassword(user, request.password);
        readEmail(user, request.email);
        readPhone(user, request.phone);
        readName(user, request.name);
        readRole(user, request.role);

        user.setGender(request.gender);
        return userQuery.save(user);
    }

    private void readUsername(User user, String username) {
        if (user.getId() != null) {
            return;
        }

        username = ReaderUtil.readString(username);

        if (!ReaderUtil.isValidUsername(username)) {
            throw new IllegalStateException("Invalid username: " + username);
        }

        if (userQuery.existByUsername(username)) {
            throw new IllegalStateException("Username " + username + " already exists");
        }

        user.setUsername(username);
    }

    private void readPassword(User user, String password) {
        if (!ReaderUtil.isValidPassword(password)) {
            throw new IllegalStateException("Invalid password: " + password);
        }

        user.setPassword(HashUtil.hash(password));
    }

    private void readEmail(User user, String email) {
        email = ReaderUtil.readString(email);
        if (!ReaderUtil.isValidEmail(email)) {
            throw new IllegalStateException("Invalid email: " + email);
        }

        user.setEmail(email);
    }

    private void readPhone(User user, String phone) {
        phone = ReaderUtil.readString(phone);
        if (phone.isEmpty()) {
            return;
        }

        if (!ReaderUtil.isValidPhoneNumber(phone)) {
            throw new IllegalStateException("Invalid phone number: " + phone);
        }

        user.setPhone(phone);
    }

    private void readName(User user, String name) {
        name = ReaderUtil.readString(name);
        if (!ReaderUtil.isValidString(name)) {
            throw new IllegalStateException("Invalid name: " + name);
        }

        user.setName(name);
    }

    private void readRole(User user, String role) {
        Role r = Role.fromString(ReaderUtil.readString(role));
        if (r != Role.TEACHER && r != Role.STUDENT) {
            throw new IllegalStateException("Invalid role: " + role);
        }

        user.setRole(r);
    }
}
