package com.example.learninghubbackend.controllers.post;

import com.example.learninghubbackend.commons.exceptions.InvalidField;
import com.example.learninghubbackend.commons.exceptions.NotFoundException;
import com.example.learninghubbackend.commons.exceptions.NotHavePermission;
import com.example.learninghubbackend.dtos.requests.post.CreateCommentRequest;
import com.example.learninghubbackend.dtos.requests.post.UpdateComment;
import com.example.learninghubbackend.dtos.requests.vote.VoteRequest;
import com.example.learninghubbackend.dtos.responses.BaseResponse;
import com.example.learninghubbackend.models.post.Comment;
import com.example.learninghubbackend.services.AppContext;
import com.example.learninghubbackend.services.post.PostService;
import com.example.learninghubbackend.services.post.comment.CommentACL;
import com.example.learninghubbackend.services.post.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/post/v1/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentACL commentACL;
    private final CommentService commentService;
    private final AppContext appContext;
    private final PostService postService;

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody UpdateComment request) {
        Comment comment = commentService.query().get(id);
        if (comment == null) {
            throw new NotFoundException("Comment not found");
        }

        if (!commentACL.canEdit(comment)) {
            throw new NotHavePermission("edit this comment");
        }

        commentService.update(comment, request.getContent());
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.success(commentService.release(comment))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Comment comment = commentService.query().get(id);
        if (comment == null) {
            throw new NotFoundException("Comment not found");
        }

        if (!commentACL.canDelete(comment)) {
            throw new NotHavePermission("delete this comment");
        }

        postService.deleteComment(comment);
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.success()
        );
    }

    @PostMapping("/{id}/vote")
    public ResponseEntity<Object> vote(@PathVariable Long id, VoteRequest request) {
        Comment comment = commentService.query().get(id);
        if (comment == null) {
            throw new NotFoundException("Comment not found");
        }

        if (!commentACL.canReact(comment)) {
            throw new NotHavePermission("vote this comment");
        }

        if (request.getVote() == null) {
            throw new InvalidField("vote");
        }

        Long userId = appContext.getUserId();
        commentService.vote(comment, request.getVote(), userId);

        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.success(commentService.release(comment))
        );
    }

    @PostMapping("/{id}/unvote")
    public ResponseEntity<Object> unvote(@PathVariable Long id) {
        Comment comment = commentService.query().get(id);
        if (comment == null) {
            throw new NotFoundException("Comment not found");
        }

        if (!commentACL.canReact(comment)) {
            throw new NotHavePermission("unvote this comment");
        }

        commentService.unvote(comment, appContext.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.success(commentService.release(comment))
        );
    }

    @PostMapping("/{id}")
    public ResponseEntity<Object> answerComment(@PathVariable Long id, @RequestBody CreateCommentRequest request) {
        Comment comment = commentService.query().get(id);
        if (comment == null) {
            throw new NotFoundException("Comment not found");
        }

        Comment newComment = postService.addComment(comment, request, appContext.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.success(newComment.release())
        );
    }
}
