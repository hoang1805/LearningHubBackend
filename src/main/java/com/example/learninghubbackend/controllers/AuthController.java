package com.example.learninghubbackend.controllers;

import com.example.learninghubbackend.commons.PropertiesData;
import com.example.learninghubbackend.commons.exceptions.NotFoundException;
import com.example.learninghubbackend.commons.exceptions.PasswordNotMatch;
import com.example.learninghubbackend.commons.exceptions.Unauthorized;
import com.example.learninghubbackend.dtos.requests.auth.LoginRequest;
import com.example.learninghubbackend.dtos.requests.user.RegisterRequest;
import com.example.learninghubbackend.dtos.responses.BaseResponse;
import com.example.learninghubbackend.models.Session;
import com.example.learninghubbackend.models.User;
import com.example.learninghubbackend.commons.ClientInfo;
import com.example.learninghubbackend.services.auth.AuthService;
import com.example.learninghubbackend.services.auth.session.SessionService;
import com.example.learninghubbackend.services.jwt.JwtPayload;
import com.example.learninghubbackend.services.jwt.JwtService;
import com.example.learninghubbackend.services.user.UserService;
import com.example.learninghubbackend.utils.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/auth/v1")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;
    private final JwtService jwtService;
    private final PropertiesData propertiesData;
    private final SessionService sessionService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        ClientInfo clientInfo = ClientInfo.getClientInfo(servletRequest);
        Session session = authService.login(loginRequest, clientInfo);
        JwtPayload payload = new JwtPayload(session.getId(), session.getUserId());
        String accessToken = jwtService.generateAccessToken(payload);
        String refreshToken = jwtService.generateRefreshToken(payload);

        servletResponse.addCookie(CookieUtil.generateCookie("access_token", accessToken, "/", propertiesData.getEnvironment().getProperty("app.jwt.expire.accessToken", "10m")));
        servletResponse.addCookie(CookieUtil.generateCookie("refresh_token", refreshToken, "/", propertiesData.getEnvironment().getProperty("app.jwt.expire.refreshToken", "60d")));

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success());
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest request) {
        if (!request.password.equals(request.confirmPassword)) {
            throw new PasswordNotMatch();
        }

        User user = userService.createUser(request);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success());
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();
        authService.logout(userId);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success());
    }

    @PostMapping("/refresh")
    public ResponseEntity<Object> refresh(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = CookieUtil.getCookie(request, "refresh_token");
        String refreshToken = cookie == null ? null : cookie.getValue();
        if (refreshToken == null) {
            throw new Unauthorized();
        }
        try {
            JwtPayload payload = jwtService.validateToken(cookie.getValue());
            Session session = sessionService.query().getSession(payload.getSessionId());
            if (session == null || session.isRevoked() || !session.getUserId().equals(payload.getUserId())) {
                throw new NotFoundException("");
            }

            ClientInfo clientInfo = ClientInfo.getClientInfo(request);
            if (!session.verify(clientInfo)) {
                throw new Unauthorized();
            }

            String accessToken = jwtService.generateAccessToken(payload);
            response.addCookie(CookieUtil.generateCookie("access_token", accessToken, "/", propertiesData.getEnvironment().getProperty("app.jwt.expire.accessToken", "10m")));
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success());
        } catch (Exception e) {
            throw new Unauthorized();
        }
    }
}
