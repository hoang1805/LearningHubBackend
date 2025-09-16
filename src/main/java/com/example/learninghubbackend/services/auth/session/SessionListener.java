package com.example.learninghubbackend.services.auth.session;

import com.example.learninghubbackend.models.Session;
import com.example.learninghubbackend.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SessionListener {
    private final SessionQuery sessionQuery;

    public void onCreated(Session session) {
        List<Session> sessions = sessionQuery.getSessionsByUserId(session.getUserId());
        for (Session s : sessions) {
            if (s.getId().equals(session.getId())) {
                continue;
            }

            s.setRevoked(true);
        }

        sessionQuery.saveAll(sessions);
    }
}
