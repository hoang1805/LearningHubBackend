package com.example.learninghubbackend.repositories.post;

import com.example.learninghubbackend.models.post.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
