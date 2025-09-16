package com.example.learninghubbackend.services.user;

import com.example.learninghubbackend.models.User;
import com.example.learninghubbackend.repositories.UserRepository;
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
}
