package com.example.learninghubbackend.services.token;

import com.example.learninghubbackend.models.Token;
import com.example.learninghubbackend.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    public Token get(UUID id) {
        Token token = tokenRepository.findById(id).orElse(null);
        if (token == null) {
            return null;
        }

        if (token.isExpired()) {
            tokenRepository.delete(token);
            return null;
        }

        return token;
    }

    public Token get(String token) {
        try {
            UUID id = UUID.fromString(token);
            return get(id);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Token save(Token token) {
        return tokenRepository.save(token);
    }

    public void delete(Token token) {
        tokenRepository.delete(token);
    }

    public void delete(UUID id) {
        tokenRepository.deleteById(id);
    }

    public void delete(String token) {
        try {
            UUID id = UUID.fromString(token);
            delete(id);
        } catch (IllegalArgumentException ignored) {
        }
    }

    public Token create(TokenData tokenData) {
        Token token = new Token();

        token.setData(tokenData.getData());
        token.setAction(tokenData.getAction());
        token.setObjectId(tokenData.getObjectId());
        token.setObjectType(tokenData.getObjectType());
        token.setExpiredAt(tokenData.getExpiredAt());

        return save(token);
    }
}
