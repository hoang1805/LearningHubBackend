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
    @Column(name = "created_at", nullable = false, updatable = false)
    private Long createdAt;

    @Column(name = "updated_at", nullable = false)
    private Long updatedAt;

    public BaseModel() {
        this.createdAt = TimerUtil.now();
        this.updatedAt = createdAt;
    }

    public BaseModel(Long createdAt, Long updatedAt) {
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
