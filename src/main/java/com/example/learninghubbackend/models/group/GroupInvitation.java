package com.example.learninghubbackend.models.group;

import com.example.learninghubbackend.commons.models.ObjectType;
import com.example.learninghubbackend.models.BaseModel;
import com.example.learninghubbackend.services.group.MembershipType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "group_invitations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupInvitation extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "invitor_id", nullable = false)
    private Long invitorId;

    @Column(name = "membership_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MembershipType membershipType;

    @Override
    public String getModelType() {
        return ObjectType.GROUP_INVITATION.getValue();
    }
}
