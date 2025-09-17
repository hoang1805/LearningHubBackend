package com.example.learninghubbackend.services.group;

import com.example.learninghubbackend.dtos.requests.group.RejectRequest;
import com.example.learninghubbackend.models.group.Group;
import com.example.learninghubbackend.models.group.GroupRequest;
import com.example.learninghubbackend.services.group.member.GroupMemberService;
import com.example.learninghubbackend.services.group.request.GroupRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupListener {
    private final GroupQuery query;
    private final GroupMemberService gm;
    private final GroupRequestService gs;

    public void onCreated(Group group) {
        gm.createGroupMember(group.getCreatorId(), group.getId(), MembershipType.CREATOR);
    }

    public void onJoined(Group group, Long userId, MembershipType membershipType) {
        gm.createGroupMember(group.getId(), userId, membershipType);
    }

    public void onApproved(Group group, GroupRequest groupRequest) {
        onJoined(group, groupRequest.getUserId(), groupRequest.getMembershipType());
        gs.remove(groupRequest);
    }

    public void onRejected(Group group, GroupRequest groupRequest, RejectRequest rejectRequest) {
        gs.remove(groupRequest);
    }

    public void onQuited(Group group, Long userId) {
        gs.remove(group.getId(), userId);
    }
}
