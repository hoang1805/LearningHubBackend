package com.example.learninghubbackend.services.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class JwtPayload {
    private Long sessionId;
    private Long userId;
}
