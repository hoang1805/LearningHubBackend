package com.example.learninghubbackend.services.post.comment;

import com.example.learninghubbackend.models.post.Comment;
import com.example.learninghubbackend.repositories.post.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentQuery {
    private final CommentRepository commentRepository;

    public Comment get(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }
}
