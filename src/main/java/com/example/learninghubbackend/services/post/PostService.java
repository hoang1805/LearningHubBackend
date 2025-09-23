package com.example.learninghubbackend.services.post;

import com.example.learninghubbackend.commons.exceptions.NotFoundException;
import com.example.learninghubbackend.commons.models.ObjectType;
import com.example.learninghubbackend.dtos.requests.post.CreateCommentRequest;
import com.example.learninghubbackend.dtos.requests.vote.VoteRequest;
import com.example.learninghubbackend.dtos.responses.post.PostRelease;
import com.example.learninghubbackend.models.post.Comment;
import com.example.learninghubbackend.models.post.Post;
import com.example.learninghubbackend.models.Vote;
import com.example.learninghubbackend.services.AppContext;
import com.example.learninghubbackend.services.post.comment.CommentService;
import com.example.learninghubbackend.services.vote.VoteService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostReader reader;
    private final PostQuery query;
    private final CommentService commentService;
    private final VoteService voteService;
    private final AppContext appContext;

    public PostQuery query() {
        return query;
    }

    public Post createPost(String content, Long creatorId) {
        Post post = new Post();
        reader.read(post, content, creatorId);

        post.setScope(Scope.PUBLIC);
        post.setGroupId(null);

        return query.save(post);
    }

    public Post createPost(String content, Long creatorId, @NonNull Long groupId) {
        Post post = new Post();
        reader.read(post, content, creatorId);

        post.setUpVotes(0L);
        post.setDownVotes(0L);
        post.setComments(0L);
        post.calculateScore();

        post.setScope(Scope.GROUP);
        post.setGroupId(groupId);

        return query.save(post);
    }

    public void editPost(Post post, String content) {
        reader.readContent(post, content);
        query.save(post);
    }

    @Transactional
    public Comment addComment(Post post, CreateCommentRequest request, Long creatorId) {
        post.addComments();
        query.save(post);

        return commentService.create(post, request.getContent(), creatorId);
    }

    @Transactional
    public Comment addComment(Comment parent, CreateCommentRequest request, Long creatorId) {
        Post post = query.get(parent.getPostId());
        if (post == null) {
            throw new NotFoundException("Post not found");
        }

        post.addComments();
        query.save(post);

        return commentService.create(parent, request.getContent(), creatorId);
    }

    @Transactional
    public void deleteComment(Comment comment) {
        Post post = query.get(comment.getPostId());
        if (post == null) {
            throw new NotFoundException("Post not found");
        }

        post.removeComments();
        query.save(post);

        commentService.query().delete(comment);
    }

    @Transactional
    public void vote(Post post, VoteRequest request, Long userId) {
        if (request.getVote() == null) {
            throw new NotFoundException("Vote not found");
        }

        Vote vote = voteService.get(post.getId(), ObjectType.COMMENT, userId);
        if (vote == null) {
            vote = new Vote(userId, ObjectType.COMMENT, post.getId());
        }
        post.removeVote(vote.getType());

        voteService.update(vote, request.getVote());
        post.addVote(vote.getType());

        query.save(post);
    }

    @Transactional
    public void unvote(Post post, Long userId) {
        Vote vote = voteService.get(post.getId(), ObjectType.COMMENT, userId);
        if (vote == null) {
            throw new NotFoundException("Vote not found");
        }

        voteService.delete(vote);

        post.removeVote(vote.getType());
        query.save(post);
    }

    public PostRelease release(Post post) {
        PostRelease data = post.release();
        Vote vote = voteService.get(post.getId(), ObjectType.POST, appContext.getUserId());
        if (vote != null) {
            data.setVote(vote.getType());
        }

        return data;
    }
}
