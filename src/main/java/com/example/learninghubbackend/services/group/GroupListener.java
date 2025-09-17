package com.example.learninghubbackend.services.group;

import com.example.learninghubbackend.models.group.Group;
import com.example.learninghubbackend.services.group.member.GroupMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupListener {
    private final GroupQuery query;
    private final GroupMemberService gm;

    public void onCreated(Group group) {
        gm.createGroupMember(group.getCreatorId(), group.getId(), MembershipType.CREATOR);
    }

    public void onJoined(Group group, Long userId, MembershipType membershipType) {
        gm.createGroupMember(group.getId(), userId, membershipType);
    }
}
