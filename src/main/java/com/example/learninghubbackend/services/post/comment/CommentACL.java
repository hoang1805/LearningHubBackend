package com.example.learninghubbackend.services.post.comment;

import com.example.learninghubbackend.models.post.Comment;
import com.example.learninghubbackend.models.post.Post;
import com.example.learninghubbackend.services.AppContext;
import com.example.learninghubbackend.services.group.GroupService;
import com.example.learninghubbackend.services.post.PostService;
import com.example.learninghubbackend.services.post.Scope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentACL implements ICommentACL {
    private final AppContext appContext;
    private final PostService postService;
    private final GroupService groupService;

    @Override
    public boolean canEdit(Comment comment) {
        Long userId = appContext.getUserId();
        if (!comment.getCreatorId().equals(userId)) {
            return false;
        }

        Post post = postService.query().get(comment.getPostId());
        if (post == null) {
            return false;
        }

        if (post.getScope() == Scope.GROUP) {
            return groupService.haveMember(post.getGroupId(), userId);
        }

        return true;
    }

    @Override
    public boolean canView(Comment object) {
        return false;
    }

    @Override
    public boolean canCreate() {
        return false;
    }

    @Override
    public boolean canDelete(Comment comment) {
        return canEdit(comment);
    }

    @Override
    public boolean canReact(Comment comment) {
        Long userId = appContext.getUserId();
        Post post = postService.query().get(comment.getPostId());
        if (post == null) {
            return false;
        }

        return post.getScope() != Scope.GROUP || groupService.haveMember(post.getGroupId(), userId);
    }
}
