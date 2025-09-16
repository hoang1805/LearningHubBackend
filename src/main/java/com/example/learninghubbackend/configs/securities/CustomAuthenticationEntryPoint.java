package com.example.learninghubbackend.configs.securities;

import com.example.learninghubbackend.dtos.responses.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        BaseResponse<?> baseResponse = BaseResponse.error(
                "You are not logged in or your session has expired.",
                HttpStatus.UNAUTHORIZED.value()
        );

        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(baseResponse));
    }
}
