package com.example.learninghubbackend.dtos.responses.post;

import com.example.learninghubbackend.services.post.vote.VoteType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class CommentReleaseCompact {
    private Long id;

    private List<Long> children;

    private Long depth;

    @JsonProperty("parent_id")
    private Long parentId;

    @JsonProperty("post_id")
    private Long postId;

    @JsonProperty("up_votes")
    private Long upVotes;

    @JsonProperty("down_votes")
    private Long downVotes;

    private Double score;

    @JsonProperty("creator_id")
    private Long creatorId;

    @JsonProperty("created_at")
    private Long createdAt;

    @JsonProperty("updated_at")
    private Long updatedAt;
}
