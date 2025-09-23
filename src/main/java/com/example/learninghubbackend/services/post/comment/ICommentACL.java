package com.example.learninghubbackend.services.post.comment;

import com.example.learninghubbackend.commons.IBaseACL;
import com.example.learninghubbackend.models.post.Comment;

public interface ICommentACL extends IBaseACL<Comment> {
    boolean canReact(Comment comment);
}
