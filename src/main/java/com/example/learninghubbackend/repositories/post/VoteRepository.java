package com.example.learninghubbackend.repositories.post;

import com.example.learninghubbackend.models.post.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
}
