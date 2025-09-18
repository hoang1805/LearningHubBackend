package com.example.learninghubbackend.services.group.invitation;

import com.example.learninghubbackend.dtos.requests.group.InviteRequest;
import com.example.learninghubbackend.models.group.Group;
import com.example.learninghubbackend.models.group.GroupInvitation;
import com.example.learninghubbackend.repositories.group.GroupInvitationRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupInvitationService {
    private final GroupInvitationRepository groupInvitationRepository;

    public void deleteByGroup(@NonNull Group group) {
        groupInvitationRepository.deleteByGroupId(group.getId());
    }

    public GroupInvitation createGroupInvitation(@NonNull Group group, Long invitorId, @NonNull InviteRequest request) {
        GroupInvitation invitation = new GroupInvitation();
        invitation.setGroupId(group.getId());
        invitation.setInvitorId(invitorId);
        invitation.setUserId(request.getInvitedUserId());
        invitation.setMembershipType(request.getMembershipType());

        return groupInvitationRepository.save(invitation);
    }
}
