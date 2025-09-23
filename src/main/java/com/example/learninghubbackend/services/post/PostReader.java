package com.example.learninghubbackend.services.post;

import com.example.learninghubbackend.commons.exceptions.InvalidField;
import com.example.learninghubbackend.models.post.Post;
import com.example.learninghubbackend.utils.ReaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostReader {
    public void read(Post post, String content, Long creatorId) {
        readContent(post, content);
        readCreator(post, creatorId);
    }

    public void readContent(Post post, String content) {
        if (!ReaderUtil.isValidString(content)) {
            throw new InvalidField("content");
        }

        post.setContent(content);
    }

    private void readCreator(Post post, Long creatorId) {
        if (creatorId == null) {
            throw new InvalidField("creator_id");
        }

        post.setCreatorId(creatorId);
    }
}
