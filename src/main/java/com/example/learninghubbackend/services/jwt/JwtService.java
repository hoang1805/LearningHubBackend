package com.example.learninghubbackend.services.jwt;

import com.example.learninghubbackend.commons.PropertiesData;
import com.example.learninghubbackend.commons.exceptions.InvalidJwtToken;
import com.example.learninghubbackend.utils.TimerUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Duration;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey key;
    private final String accessTokenExpire;
    private final String refreshTokenExpire;

    @Autowired
    public JwtService(PropertiesData propertiesData) {
        String secret = propertiesData.getEnvironment().getProperty("app.jwt.secret", "secret");
        this.key = Keys.hmacShaKeyFor(secret.getBytes());

        accessTokenExpire = propertiesData.getEnvironment().getProperty("app.jwt.expire.accessToken", "30m");
        refreshTokenExpire = propertiesData.getEnvironment().getProperty("app.jwt.expire.refreshToken", "30d");
    }

    public String generateToken(JwtPayload payload, String expire) {
        Duration expireDuration = DurationStyle.detectAndParse(expire);
        long now = TimerUtil.now();
        JwtBuilder builder = Jwts.builder()
                .subject(String.valueOf(payload.getUserId()))
                .issuedAt(new Date(now))
                .expiration(new Date(now + expireDuration.toMillis()))
                .claim("uid", payload.getUserId())
                .claim("sid", payload.getSessionId());

        return builder.signWith(key).compact();
    }

    public String generateAccessToken(JwtPayload payload) {
        return generateToken(payload, accessTokenExpire);
    }

    public String generateRefreshToken(JwtPayload payload) {
        return generateToken(payload, refreshTokenExpire);
    }

    public JwtPayload validateToken(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            return getPayload(claims);
        } catch (JwtException e) {
            throw new InvalidJwtToken();
        }
    }

    private JwtPayload getPayload(Claims claims) {
        Long sid = claims.get("sid", Long.class);
        Long uid = claims.get("uid", Long.class);

        if (uid == null || sid == null) {
            return null;
        }

        return new JwtPayload(sid, uid);
    }
}
