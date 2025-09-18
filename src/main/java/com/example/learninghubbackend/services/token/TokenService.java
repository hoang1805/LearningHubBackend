package com.example.learninghubbackend.services.token;

import com.example.learninghubbackend.commons.exceptions.CustomException;
import com.example.learninghubbackend.commons.exceptions.ServerException;
import com.example.learninghubbackend.commons.models.ObjectType;
import com.example.learninghubbackend.models.Token;
import com.example.learninghubbackend.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public List<Token> get(ObjectType objectType, Long objectId, Action action) {
        List<Token> tokens = tokenRepository.findByObjectTypeAndObjectIdAndAction(objectType, objectId, action);
        List<Token> expired = tokens.stream()
                .filter(Token::isExpired)
                .toList();

        if (!expired.isEmpty()) {
            tokenRepository.deleteAll(expired); // bulk delete thay vì từng cái
        }

        return tokens.stream().filter(token -> !token.isExpired()).collect(Collectors.toList());
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

    public void delete(ObjectType objectType, Long objectId, Action action) {
        tokenRepository.deleteByObjectTypeAndObjectIdAndAction(objectType, objectId, action);
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

        if (tokenData.getObjectType() == ObjectType.TOKEN) {
            throw new ServerException("Can not create a new token with object type is Token");
        }

        token.setData(tokenData.getData());
        token.setAction(tokenData.getAction());
        token.setObjectId(tokenData.getObjectId());
        token.setObjectType(tokenData.getObjectType());
        token.setExpiredAt(tokenData.getExpiredAt());

        return save(token);
    }
}
