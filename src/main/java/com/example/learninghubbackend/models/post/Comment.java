package com.example.learninghubbackend.models.post;

import com.example.learninghubbackend.commons.converters.JsonMapConverter;
import com.example.learninghubbackend.commons.converters.ListLongConverter;
import com.example.learninghubbackend.commons.models.ObjectType;
import com.example.learninghubbackend.models.BaseModel;
import com.example.learninghubbackend.utils.TimerUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "comments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comment extends BaseModel {
    private static final long MAX_DEPTH = 3L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    @Convert(converter = ListLongConverter.class)
    private List<Long> children;

    @Column(nullable = false)
    private Long depth;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "up_votes", nullable = false)
    private Long upVotes;

    @Column(name = "down_votes", nullable = false)
    private Long downVotes;

    @Column(nullable = false)
    private Double score;

    @Column(nullable = false)
    @Convert(converter = JsonMapConverter.class)
    private Map<String, Object> data;

    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    public Comment(String content, Long depth, Long parentId, Long creatorId) {
        super();

        this.content = content;
        this.depth = depth > MAX_DEPTH ? MAX_DEPTH : depth;
        this.parentId = parentId;
        this.creatorId = creatorId;
        
        this.children = new ArrayList<>();

        this.data = new HashMap<>();

        this.upVotes = 0L;
        this.downVotes = 0L;
        this.score = 0.0;
    }

    public Comment(String content, Long creatorId) {
        super();

        this.content = content;
        this.creatorId = creatorId;

        this.depth = 1L;
        this.parentId = null;

        this.children = new ArrayList<>();

        this.data = new HashMap<>();

        this.upVotes = 0L;
        this.downVotes = 0L;
        this.score = 0.0;
    }

    public void addVote(VoteType voteType) {
        if (voteType == VoteType.UP) {
            this.upVotes++;
        } else {
            this.downVotes++;
        }
        calculateScore();
    }

    public void removeVote(VoteType voteType) {
        if (voteType == VoteType.UP) {
            this.upVotes--;
        } else {
            this.downVotes--;
        }
        calculateScore();
    }

    public void addChildren(Long commentId) {
        this.children.add(commentId);
    }

    @Override
    public String getModelType() {
        return ObjectType.COMMENT.toString();
    }

    private void calculateScore() {
        long votes = this.upVotes - this.downVotes;

        // log10(max(|votes|, 1))
        double order = Math.log10(Math.max(Math.abs(votes), 1));

        // sign(votes)
        int sign = Long.compare(votes, 0); // -1, 0, hoặc 1

        // số giây kể từ epoch
        long seconds = TimerUtil.now() / 1000;

        // công thức chính
        this.score = order + (sign * seconds / 45000.0);
    }
}
