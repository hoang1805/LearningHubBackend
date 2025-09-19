package com.example.learninghubbackend.services.post;

import com.example.learninghubbackend.commons.IBaseACL;
import com.example.learninghubbackend.models.post.Post;

public interface IPostACL extends IBaseACL<Post> {
    boolean canCreate(Scope scope, Long groupId);
    boolean canComment(Post post);
    boolean canVote(Post post);
}
