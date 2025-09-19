package com.example.learninghubbackend.models.post;

import com.example.learninghubbackend.commons.models.ObjectType;
import com.example.learninghubbackend.models.BaseModel;
import com.example.learninghubbackend.services.post.vote.VoteType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "votes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Vote extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private VoteType type;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "object_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ObjectType objectType;

    @Column(name = "object_id", nullable = false)
    private Long objectId;

    @Override
    public String getModelType() {
        return ObjectType.VOTE.toString();
    }
}
