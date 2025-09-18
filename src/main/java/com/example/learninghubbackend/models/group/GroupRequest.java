package com.example.learninghubbackend.models.group;

import com.example.learninghubbackend.services.group.MembershipType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "group_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    private String message;

    @Column(name = "membership_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MembershipType membershipType;
}
