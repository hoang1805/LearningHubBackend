package com.example.learninghubbackend.models;

import com.example.learninghubbackend.utils.TimerUtil;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseModel {
    @Column(name = "creator_id")
    private Long creatorId;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "updatedAt")
    private Long updatedAt;

    public BaseModel() {
    }

    public BaseModel(Long creatorId) {
        this.creatorId = creatorId;
        this.createdAt = TimerUtil.now();
        this.updatedAt = createdAt;
    }

    public BaseModel(Long creatorId, Long createdAt, Long updatedAt) {
        this.creatorId = creatorId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void onUpdate() {
        this.updatedAt = TimerUtil.now();
    }

    abstract public String getModelType();

    public String getLink() {
        return null;
    }
}
