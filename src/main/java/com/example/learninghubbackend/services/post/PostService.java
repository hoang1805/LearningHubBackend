package com.example.learninghubbackend.services.post;

import com.example.learninghubbackend.commons.models.ObjectType;
import com.example.learninghubbackend.dtos.requests.post.CreateCommentRequest;
import com.example.learninghubbackend.dtos.responses.post.PostRelease;
import com.example.learninghubbackend.models.post.Comment;
import com.example.learninghubbackend.models.post.Post;
import com.example.learninghubbackend.models.post.Vote;
import com.example.learninghubbackend.services.AppContext;
import com.example.learninghubbackend.services.post.comment.CommentService;
import com.example.learninghubbackend.services.post.vote.VoteService;
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
    public Comment addComment(Post post, CreateCommentRequest request, Long creatorId, Long parentId) {
        post.addComments();
        query.save(post);

        return commentService.create(post, request.getContent(), creatorId, parentId);
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
