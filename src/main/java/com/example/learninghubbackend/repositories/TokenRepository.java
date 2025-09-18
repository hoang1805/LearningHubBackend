package com.example.learninghubbackend.repositories;

import com.example.learninghubbackend.commons.models.ObjectType;
import com.example.learninghubbackend.models.Token;
import com.example.learninghubbackend.services.token.Action;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {
    List<Token> findByObjectTypeAndObjectIdAndAction(ObjectType objectType, Long objectId, Action action);

    void deleteByObjectTypeAndObjectIdAndAction(ObjectType objectType, Long objectId, Action action);
}