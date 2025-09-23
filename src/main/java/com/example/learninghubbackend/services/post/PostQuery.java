package com.example.learninghubbackend.services.post;

import com.example.learninghubbackend.models.post.Post;
import com.example.learninghubbackend.repositories.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostQuery {
    private final PostRepository postRepository;

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public Post get(Long id) {
        return postRepository.findById(id).orElse(null);
    }
}
