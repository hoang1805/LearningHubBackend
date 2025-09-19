package com.example.learninghubbackend.controllers.post;

import com.example.learninghubbackend.commons.exceptions.NotFoundException;
import com.example.learninghubbackend.commons.exceptions.NotHavePermission;
import com.example.learninghubbackend.dtos.requests.post.CreateCommentRequest;
import com.example.learninghubbackend.dtos.requests.post.CreateRequest;
import com.example.learninghubbackend.dtos.requests.post.EditRequest;
import com.example.learninghubbackend.dtos.responses.BaseResponse;
import com.example.learninghubbackend.models.post.Comment;
import com.example.learninghubbackend.models.post.Post;
import com.example.learninghubbackend.services.AppContext;
import com.example.learninghubbackend.services.post.PostACL;
import com.example.learninghubbackend.services.post.PostService;
import com.example.learninghubbackend.services.post.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/post/v1")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final AppContext appContext;
    private final PostACL postACL;
    private final CommentService commentService;

    @PostMapping("")
    public ResponseEntity<Object> createPost(@RequestBody CreateRequest request) {
        Long userId = appContext.getUserId();
        Post post = postService.createPost(request.getContent(), userId);

        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.success(post.release())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editPost(@PathVariable("id") Long id, @RequestBody EditRequest request) {
        Post post = postService.query().get(id);
        if (post == null) {
            throw new NotFoundException("Post not found");
        }

        if (!postACL.canEdit(post)) {
            throw new NotHavePermission("edit this post");
        }

        postService.editPost(post, request.getContent());
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.success(postService.release(post))
        );
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> createComment(@PathVariable("id") Long id, @RequestBody CreateCommentRequest request) {
        Post post = postService.query().get(id);
        if (post == null) {
            throw new NotFoundException("Post not found");
        }

        if (!postACL.canComment(post)) {
            throw new NotHavePermission("comment this post");
        }

        Comment comment = postService.addComment(post, request, appContext.getUserId(), null);
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.success(comment.release())
        );
    }
}
