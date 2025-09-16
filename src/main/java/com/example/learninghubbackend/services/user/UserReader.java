package com.example.learninghubbackend.services.user;

import com.example.learninghubbackend.commons.exceptions.AlreadyExists;
import com.example.learninghubbackend.commons.exceptions.InvalidField;
import com.example.learninghubbackend.commons.exceptions.InvalidPassword;
import com.example.learninghubbackend.commons.exceptions.PasswordNotMatch;
import com.example.learninghubbackend.dtos.requests.user.ChangePasswordRequest;
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

    public User changePassword(User user, ChangePasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new PasswordNotMatch("New password");
        }

        if (!ReaderUtil.isValidPassword(request.getOldPassword())) {
            throw new InvalidPassword();
        }

        if (!ReaderUtil.isValidPassword(request.getNewPassword())) {
            throw new InvalidPassword("new password");
        }

        if (!HashUtil.verify(user.getPassword(), request.getOldPassword())) {
            throw new InvalidField("password", "Wrong password");
        }

        readPassword(user, request.getNewPassword());
        return userQuery.save(user);
    }

    private void readUsername(User user, String username) {
        if (user.getId() != null) {
            return;
        }

        username = ReaderUtil.readString(username);

        if (!ReaderUtil.isValidUsername(username)) {
            throw new InvalidField("username");
        }

        if (userQuery.existByUsername(username)) {
            throw new AlreadyExists("Username", username);
        }

        user.setUsername(username);
    }

    private void readPassword(User user, String password) {
        if (!ReaderUtil.isValidPassword(password)) {
            throw new InvalidPassword();
        }

        user.setPassword(HashUtil.hash(password));
    }

    private void readEmail(User user, String email) {
        email = ReaderUtil.readString(email);
        if (!ReaderUtil.isValidEmail(email)) {
            throw new InvalidField("email");
        }

        user.setEmail(email);
    }

    private void readPhone(User user, String phone) {
        phone = ReaderUtil.readString(phone);
        if (phone.isEmpty()) {
            return;
        }

        if (!ReaderUtil.isValidPhoneNumber(phone)) {
            throw new InvalidField("phone number");
        }

        user.setPhone(phone);
    }

    private void readName(User user, String name) {
        name = ReaderUtil.readString(name);
        if (!ReaderUtil.isValidString(name)) {
            throw new InvalidField("name");
        }

        user.setName(name);
    }

    private void readRole(User user, String role) {
        Role r = Role.fromString(ReaderUtil.readString(role));
        if (r != Role.TEACHER && r != Role.STUDENT) {
            throw new InvalidField("role");
        }

        user.setRole(r);
    }
}
