package com.example.learninghubbackend.services.post.comment;

import com.example.learninghubbackend.commons.exceptions.NotFoundException;
import com.example.learninghubbackend.commons.models.ObjectType;
import com.example.learninghubbackend.dtos.responses.post.CommentRelease;
import com.example.learninghubbackend.models.post.Comment;
import com.example.learninghubbackend.models.post.Post;
import com.example.learninghubbackend.models.Vote;
import com.example.learninghubbackend.services.AppContext;
import com.example.learninghubbackend.services.vote.VoteService;
import com.example.learninghubbackend.services.vote.VoteType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentQuery query;
    private final CommentReader reader;
    private final VoteService voteService;
    private final AppContext appContext;

    public CommentQuery query() {
        return query;
    }

    public Comment create(Post post, String content, Long creatorId) {
        Comment comment = new Comment(content, creatorId, post.getId());
        return query.save(comment);
    }

    public Comment create(Comment parent, String content, Long creatorId) {
        if (parent == null) {
            throw new NotFoundException("Comment not found");
        }

        Comment comment = new Comment(content, parent.getDepth() + 1, parent.getId(), creatorId, parent.getPostId());
        query.save(comment);

        parent.addChildren(comment.getId());
        query.save(parent);

        return comment;
    }

    public void update(Comment comment, String content) {
        reader.readContent(comment, content);
        query.save(comment);
    }

    public CommentRelease release(@NonNull Comment comment) {
        CommentRelease release = comment.release();
        Vote vote = voteService.get(comment.getId(), ObjectType.COMMENT, appContext.getUserId());
        if (vote == null) {
            return release;
        }

        release.setVote(vote.getType());
        return release;
    }

    @Transactional
    public void vote(@NonNull Comment comment, VoteType type, Long userId) {
        Vote vote = voteService.get(comment.getId(), ObjectType.COMMENT, userId);
        if (vote == null) {
            vote = new Vote(userId, ObjectType.COMMENT, comment.getId());
        }

        comment.removeVote(vote.getType());

        boolean changed = voteService.update(vote, type);
        comment.addVote(type);
        query.save(comment);

        if (!changed) {
            // TODO: notification
        }
    }

    @Transactional
    public void unvote(@NonNull Comment comment, Long userId) {
        Vote vote = voteService.get(comment.getId(), ObjectType.COMMENT, userId);
        if (vote == null) {
            throw new NotFoundException("Vote not found");
        }

        voteService.delete(vote);
        comment.removeVote(vote.getType());

        query.save(comment);
    }
}
