package com.example.learninghubbackend.services.auth;

import com.example.learninghubbackend.commons.ClientInfo;
import com.example.learninghubbackend.commons.exceptions.InvalidLogin;
import com.example.learninghubbackend.commons.exceptions.InvalidPassword;
import com.example.learninghubbackend.dtos.requests.auth.LoginRequest;
import com.example.learninghubbackend.models.Session;
import com.example.learninghubbackend.models.User;
import com.example.learninghubbackend.services.auth.session.SessionService;
import com.example.learninghubbackend.services.user.UserService;
import com.example.learninghubbackend.utils.HashUtil;
import com.example.learninghubbackend.utils.ReaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final SessionService sessionService;

    @Transactional
    public Session login(LoginRequest loginRequest, ClientInfo clientInfo) {
        User user = userService.query().getUserByUsername(loginRequest.username);
        if (user == null) {
            throw new InvalidLogin();
        }

        String password = loginRequest.password;
        if (!ReaderUtil.isValidPassword(password)) {
            throw new InvalidPassword();
        }

        if (!HashUtil.verify(user.getPassword(), password)) {
            throw new InvalidLogin();
        }

        sessionService.query().revokeByUser(user.getId());

        Session session = sessionService.createSession(user, clientInfo);
        return sessionService.query().save(session);
    }

    public void logout(Long userId) {
        sessionService.query().revokeByUser(userId);
    }
}
