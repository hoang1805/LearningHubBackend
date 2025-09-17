package com.example.learninghubbackend.services.auth.session;

import com.example.learninghubbackend.models.Session;
import com.example.learninghubbackend.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SessionQuery {
    private final SessionRepository sessionRepository;

    public Session getSession(Long sessionId) {
        return sessionRepository.findById(sessionId).orElse(null);
    }

    public void revokeByUser(Long userId) {
        List<Session> sessions = sessionRepository.findAllByUserIdAndRevokedFalse(userId);
        for (Session session : sessions) {
            session.setRevoked(true);
        }
        sessionRepository.saveAll(sessions);
    }

    public List<Session> getSessionsByUserId(Long userId) {
        return sessionRepository.findAllByUserIdAndRevokedFalse(userId);
    }

    public void revoke(Long sessionId) {
        Session session = getSession(sessionId);
        if (session == null) {
            throw new IllegalStateException("Session not found");
        }

        session.setRevoked(true);
        sessionRepository.save(session);
    }

    public Session save(Session session) {
        return sessionRepository.save(session);
    }

    public List<Session> saveAll(List<Session> sessions) {
        return sessionRepository.saveAll(sessions);
    }
}
