package com.example.learninghubbackend.repositories.post;

import com.example.learninghubbackend.commons.models.ObjectType;
import com.example.learninghubbackend.models.post.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByObjectIdAndObjectTypeAndUserId(Long objectId, ObjectType objectType, Long userId);
}
