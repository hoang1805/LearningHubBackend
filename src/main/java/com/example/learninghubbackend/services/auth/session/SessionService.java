package com.example.learninghubbackend.services.auth.session;

import com.example.learninghubbackend.commons.ClientInfo;
import com.example.learninghubbackend.models.Session;
import com.example.learninghubbackend.models.User;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionQuery sessionQuery;
    private final SessionListener sessionListener;

    public SessionQuery query() {
        return sessionQuery;
    }

    public SessionListener listener() {
        return sessionListener;
    }

    @Transactional
    public Session createSession(@NonNull User user, @NonNull ClientInfo clientInfo) {
        Session session = new Session();
        session.setUserId(user.getId());
        session.setDevice(clientInfo.getDevice());
        session.setIpAddress(clientInfo.getIpAddress());
        session.setBrowser(clientInfo.getBrowser());
        session.setOs(clientInfo.getOs());
        session.setRevoked(false);

        sessionQuery.save(session);
        sessionListener.onCreated(session);
        return session;
    }
}
