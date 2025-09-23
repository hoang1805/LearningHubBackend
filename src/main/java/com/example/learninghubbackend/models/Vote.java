package com.example.learninghubbackend.models;

import com.example.learninghubbackend.commons.models.ObjectType;
import com.example.learninghubbackend.services.vote.VoteType;
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

    public Vote(Long userId, ObjectType objectType, Long objectId) {
        super();
        this.type = null;
        this.userId = userId;
        this.objectType = objectType;
        this.objectId = objectId;
    }

    @Override
    public String getModelType() {
        return ObjectType.VOTE.toString();
    }
}
