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
@Table(name = "group_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupMember extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "membership_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MembershipType membershipType;

    public GroupMember(Long groupId, Long userId, MembershipType membershipType) {
        super();
        this.groupId = groupId;
        this.userId = userId;
        this.membershipType = membershipType;
    }

    public boolean isCreator() {
        return membershipType == MembershipType.CREATOR;
    }

    public boolean isManager() {
        return membershipType == MembershipType.MANAGER;
    }

    @Override
    public String getModelType() {
        return ObjectType.GROUP_MEMBER.getValue();
    }
}
