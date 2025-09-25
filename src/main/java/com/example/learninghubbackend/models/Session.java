package com.example.learninghubbackend.models;

import com.example.learninghubbackend.commons.ClientInfo;
import com.example.learninghubbackend.commons.models.ObjectType;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private boolean revoked;

    private String device;

    private String os;

    private String browser;

    @Column(name = "ip_address", nullable = false)
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

    public boolean verify(@NonNull ClientInfo clientInfo) {
        return this.getIpAddress().equals(clientInfo.getIpAddress())
                && this.getOs().equals(clientInfo.getOs())
                && this.getBrowser().equals(clientInfo.getBrowser())
                && this.getDevice().equals(clientInfo.getDevice());
    }

    @Override
    public String getModelType() {
        return ObjectType.SESSION.getValue();
    }
}
