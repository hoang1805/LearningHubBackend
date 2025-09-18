package com.example.learninghubbackend.models;

import com.example.learninghubbackend.commons.annotations.generatedUuidV7.GeneratedUuidV7;
import com.example.learninghubbackend.commons.converters.JsonMapConverter;
import com.example.learninghubbackend.commons.models.ObjectType;
import com.example.learninghubbackend.services.token.Action;
import com.example.learninghubbackend.utils.TimerUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Token extends BaseModel {
    @Id
    @GeneratedUuidV7
    private UUID id;

    @Column(name = "object_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ObjectType objectType;

    @Column(name = "object_id", nullable = false)
    private Long objectId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Action action;

    @Convert(converter = JsonMapConverter.class)
    @Column(columnDefinition = "TEXT")
    private Map<String, Object> data;

    @Column(name = "expired_at")
    private Long expiredAt;

    @Override
    public String getModelType() {
        return ObjectType.TOKEN.toString();
    }

    public boolean isExpired() {
        return expiredAt != null && expiredAt <= TimerUtil.now();
    }
}
