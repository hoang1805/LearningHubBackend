package com.example.learninghubbackend.dtos.responses.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class PostReleaseCompact {
    private Long id;

    @JsonProperty("up_votes")
    private Long upVotes;

    @JsonProperty("down_votes")
    private Long downVotes;

    private Long comments;

    private BigDecimal score;

    @JsonProperty("creator_id")
    private Long creatorId;

    @JsonProperty("created_at")
    private Long createdAt;

    @JsonProperty("updated_at")
    private Long updatedAt;
}
