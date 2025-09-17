package com.example.learninghubbackend.models.group;

import com.example.learninghubbackend.commons.converters.ListLongConverter;
import com.example.learninghubbackend.commons.models.ObjectType;
import com.example.learninghubbackend.commons.models.Releasable;
import com.example.learninghubbackend.dtos.responses.group.GroupRelease;
import com.example.learninghubbackend.dtos.responses.group.GroupReleaseCompact;
import com.example.learninghubbackend.models.BaseModel;
import com.example.learninghubbackend.services.group.RegistrationPolicy;
import com.example.learninghubbackend.services.group.Scope;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Group extends BaseModel implements Releasable<GroupRelease, GroupReleaseCompact> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = ListLongConverter.class)
    private List<Long> members;

    @Column(name = "max_member")
    private Integer maxMember;

    @Column(name = "registration_policy")
    @Enumerated(EnumType.STRING)
    private RegistrationPolicy registrationPolicy;

    @Enumerated(EnumType.STRING)
    private Scope scope;

    @Column(name = "creator_id")
    private Long creatorId;

    public boolean haveMember(Long userId) {
        if (members == null) {
            members = new ArrayList<>();
        }

        return members.contains(userId);
    }

    public void addMember(Long userId) {
        if (members == null) {
            members = new ArrayList<>();
        }

        if (members.size() >= maxMember || !haveMember(userId)) {
            members.add(userId);
        }
    }

    public void removeMember(Long userId) {
        if (members == null) {
            members = new ArrayList<>();
        }

        if (haveMember(userId)) {
            members.remove(userId);
        }
    }

    @Override
    public String getModelType() {
        return ObjectType.GROUP.getValue();
    }

    @Override
    public GroupRelease release() {
        GroupRelease release = new GroupRelease();
        return release.setId(id).setName(name).setDescription(description)
                .setMembers(members).setScope(scope).setCreatorId(creatorId)
                .setCreatedAt(getCreatedAt()).setUpdatedAt(getUpdatedAt());
    }

    @Override
    public GroupReleaseCompact releaseCompact() {
        GroupReleaseCompact releaseCompact = new GroupReleaseCompact();
        return releaseCompact.setId(id).setName(name)
                .setScope(scope).setCreatorId(creatorId).setCreatedAt(getCreatedAt());
    }
}
