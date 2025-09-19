package com.example.learninghubbackend.services.post.vote;

import com.example.learninghubbackend.commons.models.ObjectType;
import com.example.learninghubbackend.models.post.Vote;
import com.example.learninghubbackend.repositories.post.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;

    public Vote get(Long id) {
        return voteRepository.findById(id).orElse(null);
    }

    public Vote get(Long objectId, ObjectType objectType, Long userId) {
        return voteRepository.findByObjectIdAndObjectTypeAndUserId(objectId, objectType, userId).orElse(null);
    }

    public Vote save(Vote vote) {
        return voteRepository.save(vote);
    }

    public void update(Vote vote, VoteType type) {
        vote.setType(type);
        voteRepository.save(vote);
    }

    public void delete(Vote vote) {
        voteRepository.delete(vote);
    }
}
