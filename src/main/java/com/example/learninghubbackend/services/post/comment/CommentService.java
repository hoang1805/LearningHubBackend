package com.example.learninghubbackend.services.post.comment;

import com.example.learninghubbackend.commons.exceptions.NotFoundException;
import com.example.learninghubbackend.commons.models.ObjectType;
import com.example.learninghubbackend.dtos.responses.post.CommentRelease;
import com.example.learninghubbackend.models.post.Comment;
import com.example.learninghubbackend.models.post.Post;
import com.example.learninghubbackend.models.post.Vote;
import com.example.learninghubbackend.services.AppContext;
import com.example.learninghubbackend.services.post.vote.VoteService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Comment create(Post post, String content, Long creatorId, Long parentId) {
        Comment comment = null;
        if (parentId != null) {
            Comment parent = query.get(parentId);
            if (parent == null) {
                throw new NotFoundException("Comment not found");
            }

            comment = new Comment(content, parent.getDepth() + 1, parentId, creatorId, post.getId());
        } else {
            comment = new Comment(content, creatorId, post.getId());
        }

        return query.save(comment);
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
}
