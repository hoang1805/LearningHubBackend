package com.example.learninghubbackend.services.user;

import com.example.learninghubbackend.commons.exceptions.AlreadyExists;
import com.example.learninghubbackend.commons.exceptions.InvalidField;
import com.example.learninghubbackend.commons.exceptions.InvalidPassword;
import com.example.learninghubbackend.commons.exceptions.PasswordNotMatch;
import com.example.learninghubbackend.dtos.requests.user.ChangeInformation;
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
        readGender(user, request.gender);

        return userQuery.save(user);
    }

    public void changePassword(User user, ChangePasswordRequest request) {
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
        userQuery.save(user);
    }

    public void changeInformation(User user, ChangeInformation changeInformation) {
        if (changeInformation.getName() != null) {
            readName(user, changeInformation.getName());
        }

        if (changeInformation.getGender() != null) {
            readGender(user, changeInformation.getGender());
        }

        if (changeInformation.getPhone() != null) {
            readPhone(user, changeInformation.getPhone());
        }

        if (changeInformation.getEmail() != null) {
            readEmail(user, changeInformation.getEmail());
        }

        userQuery.save(user);
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

        if (!user.getEmail().equals(email) && userQuery.existByEmail(email)) {
            throw new AlreadyExists("Email", email);
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

    private void readGender(User user, Gender gender) {
        if (gender == null) {
            user.setGender(Gender.OTHER);
            return;
        }

        user.setGender(gender);
    }
}
