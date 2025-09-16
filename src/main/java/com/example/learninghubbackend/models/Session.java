package com.example.learninghubbackend.models;

import com.example.learninghubbackend.commons.ObjectType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Session extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private boolean revoked;

    private String device;

    private String os;

    private String browser;

    @Column(name = "ip_address")
    private String ipAddress;

    public Session(Long id, Long userId, boolean revoked, String device, String os, String browser, String ipAddress, Long createdAt, Long updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.userId = userId;
        this.revoked = revoked;
        this.device = device;
        this.os = os;
        this.browser = browser;
        this.ipAddress = ipAddress;
    }

    @Override
    public String getModelType() {
        return ObjectType.SESSION.getValue();
    }
}
