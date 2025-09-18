package com.example.learninghubbackend.services.group;

import com.example.learninghubbackend.dtos.requests.group.RejectRequest;
import com.example.learninghubbackend.models.group.Group;
import com.example.learninghubbackend.models.group.GroupInvitation;
import com.example.learninghubbackend.models.group.GroupRequest;
import com.example.learninghubbackend.services.group.invitation.GroupInvitationService;
import com.example.learninghubbackend.services.group.member.GroupMemberService;
import com.example.learninghubbackend.services.group.request.GroupRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupListener {
    private final GroupMemberService gm;
    private final GroupRequestService gs;
    private final GroupInvitationService gi;

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

    public void onKicked(Group group, Long sourceUserId, Long targetUserId) {
        gs.remove(group.getId(), targetUserId);
    }

    public void onAcceptInvitation(Group group, GroupInvitation invitation) {
        onJoined(group, invitation.getUserId(), invitation.getMembershipType());
        gi.remove(invitation);
    }

    public void onRejectInvitation(Group group, GroupInvitation invitation) {
        gi.remove(invitation);
    }
}
