package com.example.learninghubbackend.repositories;

import com.example.learninghubbackend.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {
}

