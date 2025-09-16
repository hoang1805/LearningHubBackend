package com.example.learninghubbackend.configs.filters;

import com.example.learninghubbackend.commons.ClientInfo;
import com.example.learninghubbackend.models.Session;
import com.example.learninghubbackend.models.User;
import com.example.learninghubbackend.services.auth.session.SessionService;
import com.example.learninghubbackend.services.jwt.JwtPayload;
import com.example.learninghubbackend.services.jwt.JwtService;
import com.example.learninghubbackend.services.user.UserService;
import com.example.learninghubbackend.utils.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final SessionService sessionService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        Cookie accessToken = CookieUtil.getCookie(request, "access_token");
        String accessTokenValue = accessToken != null ? accessToken.getValue() : null;
        if (accessTokenValue == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            JwtPayload payload = jwtService.validateToken(accessTokenValue);
            ClientInfo clientInfo = ClientInfo.getClientInfo(request);
            Session session = sessionService.query().getSession(payload.getSessionId());
            boolean active = false;
            if (session != null && session.getUserId().equals(payload.getUserId()) && !session.isRevoked()) {
                active = verifyClient(clientInfo, session);
            }

            if (active) {
                User user = userService.query().getUser(payload.getUserId());
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(session.getUserId(), null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                SecurityContextHolder.clearContext();
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private boolean verifyClient(@NonNull ClientInfo clientInfo, @NonNull Session session) {
        return clientInfo.getIpAddress().equals(session.getIpAddress())
                && clientInfo.getOs().equals(session.getOs())
                && clientInfo.getBrowser().equals(session.getBrowser())
                && clientInfo.getDevice().equals(session.getDevice());
    }
}
