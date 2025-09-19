package com.example.learninghubbackend.services.post.comment;

import com.example.learninghubbackend.commons.exceptions.InvalidField;
import com.example.learninghubbackend.models.post.Comment;
import com.example.learninghubbackend.utils.ReaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentReader {
    public void readContent(Comment comment, String content) {
        if (!ReaderUtil.isValidString(content)) {
            throw new InvalidField("content");
        }

        comment.setContent(content);
    }
}
