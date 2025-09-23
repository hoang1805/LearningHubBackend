package com.example.learninghubbackend.models.post;

import com.example.learninghubbackend.commons.models.ObjectType;
import com.example.learninghubbackend.commons.models.Releasable;
import com.example.learninghubbackend.dtos.responses.post.PostRelease;
import com.example.learninghubbackend.dtos.responses.post.PostReleaseCompact;
import com.example.learninghubbackend.models.BaseModel;
import com.example.learninghubbackend.services.post.Scope;
import com.example.learninghubbackend.services.vote.VoteType;
import com.example.learninghubbackend.utils.TimerUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "post")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Post extends BaseModel implements Releasable<PostRelease, PostReleaseCompact> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Scope scope;

    @Column(nullable = false)
    private Long upVotes;

    @Column(nullable = false)
    private Long downVotes;

    @Column(nullable = false)
    private Long comments;

    @Column(nullable = false, precision = 30, scale = 15)
    private BigDecimal score;

    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    @Column(name = "group_id")
    private Long groupId;

    public void addVote(VoteType type) {
        if (type == null) {
            return;
        }

        upVotes += type == VoteType.UP ? 1 : 0;
        downVotes += type == VoteType.DOWN ? 1 : 0;
        calculateScore();
    }

    public void removeVote(VoteType type) {
        if (type == null) {
            return;
        }

        downVotes -= type == VoteType.DOWN ? 1 : 0;
        upVotes -= type == VoteType.UP ? 1 : 0;
        calculateScore();
    }

    public void addComments() {
        comments++;
        calculateScore();
    }

    public void removeComments() {
        comments--;
        calculateScore();
    }

    @Override
    public String getModelType() {
        return ObjectType.POST.toString();
    }

    @Override
    public PostRelease release() {
        PostRelease data = new PostRelease();
        return data.setId(id).setContent(content).setCreatorId(creatorId).setVote(null)
                .setUpVotes(upVotes).setDownVotes(downVotes).setComments(comments).setScore(score)
                .setCreatedAt(getCreatedAt()).setUpdatedAt(getUpdatedAt());
    }

    @Override
    public PostReleaseCompact releaseCompact() {
        PostReleaseCompact data = new PostReleaseCompact();
        return data.setId(id).setCreatorId(creatorId)
                .setUpVotes(upVotes).setDownVotes(downVotes).setComments(comments).setScore(score)
                .setCreatedAt(getCreatedAt()).setUpdatedAt(getUpdatedAt());
    }

    public void calculateScore() {
        // Tham số tinh chỉnh
        double alpha = 0.5;   // trọng số cho comment
        double beta = 3600;   // 1 giờ, tránh chia cho 0
        double gamma = 1.5;   // hệ số suy giảm theo thời gian

        double votes = upVotes - downVotes;
        double numerator = votes + alpha * comments;
        double denominator = Math.pow(1.0 * (TimerUtil.now() - getCreatedAt()) / 1000 + beta, gamma);

        this.score = BigDecimal.valueOf(numerator / denominator);
    }
}
