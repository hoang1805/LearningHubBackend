package com.example.learninghubbackend.repositories;

import com.example.learninghubbackend.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findAllByUserIdAndRevokedFalse(Long userId);
}
