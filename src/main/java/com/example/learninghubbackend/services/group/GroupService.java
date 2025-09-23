package com.example.learninghubbackend.services.group;

import com.example.learninghubbackend.commons.PropertiesData;
import com.example.learninghubbackend.commons.exceptions.*;
import com.example.learninghubbackend.commons.exceptions.group.AlreadyIn;
import com.example.learninghubbackend.commons.exceptions.group.GroupReachLimit;
import com.example.learninghubbackend.commons.models.ObjectType;
import com.example.learninghubbackend.dtos.requests.group.*;
import com.example.learninghubbackend.models.Token;
import com.example.learninghubbackend.models.group.Group;
import com.example.learninghubbackend.models.group.GroupInvitation;
import com.example.learninghubbackend.models.group.GroupMember;
import com.example.learninghubbackend.models.group.GroupRequest;
import com.example.learninghubbackend.services.group.invitation.GroupInvitationService;
import com.example.learninghubbackend.services.group.member.GroupMemberService;
import com.example.learninghubbackend.services.group.request.GroupRequestService;
import com.example.learninghubbackend.services.token.Action;
import com.example.learninghubbackend.services.token.TokenData;
import com.example.learninghubbackend.services.token.TokenService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupQuery query;
    private final GroupListener listener;
    private final GroupReader reader;
    private final PropertiesData propertiesData;
    private final GroupRequestService gr;
    private final GroupMemberService gm;
    private final GroupInvitationService gi;
    private final TokenService tokenService;

    public GroupQuery query() {
        return query;
    }

    public GroupListener listener() {
        return listener;
    }

    public GroupReader reader() {
        return reader;
    }

    @Transactional
    public Group createGroup(Long userId, CreateGroupRequest request) {
        Group group = new Group();
        group.setMaxMember(propertiesData.getEnvironment().getProperty("app.group.limit", Integer.class));

        reader.read(group, request);

        group.addMember(userId);
        group.setCreatorId(userId);

        query.save(group);
        if (group.getRegistrationPolicy() == RegistrationPolicy.TOKEN_BASED) {
            generateToken(group);
        }

        listener.onCreated(group);

        return group;
    }

    public void updateGroup(Group group, ChangeInformationRequest request) {
        reader.read(group, request);
        query.save(group);
    }

    @Transactional
    public void updateGroup(Group group, ChangeScopeRequest request) {
        boolean hasChange = reader.read(group, request);
        query.save(group);
        if (group.getRegistrationPolicy() == RegistrationPolicy.TOKEN_BASED) {
            generateToken(group);
        } else {
            deactivateToken(group);
        }

        if (!hasChange) {
            return;
        }

        List<GroupRequest> requests = gr.getByGroup(group.getId());

        if (group.getScope().equals(Scope.PUBLIC)) {
            approveRequests(requests, group);
            if (group.getRegistrationPolicy() == RegistrationPolicy.REQUEST_APPROVAL) {
                removeInvitations(group);
            }
        }

        if (group.getScope().equals(Scope.PRIVATE)) {
            rejectRequests(requests, group, new RejectRequest("The group is private."));
        }
    }

    @Transactional
    public void joinGroup(Long userId, JoinRequest request) {
        Group group = query.getById(request.getGroupId());
        if (group == null) {
            throw new NotFoundException("Group not found.");
        }

        if (request.getMembershipType() != MembershipType.SPECTATOR
                && request.getMembershipType() != MembershipType.PARTICIPANT) {
            throw new InvalidField("membership_type");
        }

        if (group.getScope() == Scope.PRIVATE) {
            throw new NotFoundException("Group not found");
        }

        if (group.haveMember(userId)) {
            throw new AlreadyIn();
        }

        if (group.getMembers().size() >= group.getMaxMember()) {
            throw new GroupReachLimit();
        }

        // RegistrationPolicy.OPEN
        if (group.getRegistrationPolicy() == RegistrationPolicy.OPEN) {
            group.addMember(userId);
            query.save(group);

            listener.onJoined(group, userId, request.getMembershipType());
            return ;
        }

        // RegistrationPolicy.REQUEST_APPROVAL
        GroupRequest groupRequest = gr.createGroupRequest(userId, request);
    }

    @Transactional
    public void joinGroup(Group group, Long userId, JoinByToken request) {
        if (request.getMembershipType() != MembershipType.SPECTATOR
                && request.getMembershipType() != MembershipType.PARTICIPANT) {
            throw new InvalidField("membership_type");
        }

        if (group.haveMember(userId)) {
            throw new AlreadyIn();
        }

        if (group.getMembers().size() >= group.getMaxMember()) {
            throw new GroupReachLimit();
        }

        group.addMember(userId);
        query.save(group);

        listener.onJoined(group, userId, request.getMembershipType());
    }

    @Transactional
    public void approveRequest(GroupRequest request, Group group) {
        approveRequest(request, group, true);
    }

    @Transactional
    public void approveRequest(GroupRequest request, Group group, boolean saved) {
        if (request.getMembershipType() != MembershipType.SPECTATOR
                && request.getMembershipType() != MembershipType.PARTICIPANT) {
            throw new InvalidField("membership_type");
        }

        if (group.getScope() == Scope.PRIVATE) {
            throw new NotApprove("request");
        }

        if (group.haveMember(request.getUserId())) {
            throw new AlreadyIn();
        }

        if (group.getMembers().size() >= group.getMaxMember()) {
            throw new GroupReachLimit();
        }

        group.addMember(request.getUserId());
        if (saved) {
            query.save(group);
        }

        listener.onApproved(group, request);
    }

    @Transactional
    public void rejectRequest(Group group, GroupRequest groupRequest, RejectRequest request) {
        listener.onRejected(group, groupRequest, request);
    }

    @Transactional
    public void quit(Group group, Long userId) {
        if (group.getCreatorId().equals(userId)) {
            throw new BadRequest("You cannot quit this group because you are creator.");
        }

        group.removeMember(userId);
        query.save(group);

        listener.onQuited(group, userId);
    }

    /**
     * Kick a member out of a group.
     *
     * @param group target group
     * @param sourceId user will kick
     * @param targetId user to be kicked
     */
    @Transactional
    public void kick(Group group, Long sourceId, Long targetId) {
        if (sourceId.equals(targetId)) {
            throw new BadRequest("You cannot kick this group because you are source.");
        }

        if (group.getCreatorId().equals(targetId)) {
            throw new BadRequest("You cannot kick this person because you are creator.");
        }

        GroupMember source = gm.getGroupMember(sourceId, group.getId());
        GroupMember target = gm.getGroupMember(targetId, group.getId());
        if (source == null || target == null) {
            throw new NotFoundException("Group member not found.");
        }

        if (source.getMembershipType().compareTo(target.getMembershipType()) <= 0) {
            throw new NotHavePermission("kick this person");
        }

        group.removeMember(targetId);
        query.save(group);

        listener.onKicked(group, targetId, sourceId);
    }

    @Transactional
    public void approveRequests(List<GroupRequest> groupRequests, Group group) {
        for (GroupRequest groupRequest : groupRequests) {
            try {
                approveRequest(groupRequest, group, false);
            }
            catch (GroupReachLimit e) {
                rejectRequest(group, groupRequest, new RejectRequest(e.getMessage()));
            }
        }

        query.save(group);
    }

    @Transactional
    public void rejectRequests(List<GroupRequest> groupRequests, Group group, RejectRequest request) {
        for (GroupRequest groupRequest : groupRequests) {
            rejectRequest(group, groupRequest, request);
        }
    }

    @Transactional
    public void removeInvitations(Group group) {
        gi.deleteByGroup(group);
    }

    @Transactional
    public void invite(Group group, Long invitorId, InviteRequest request) {
        if (request.getMembershipType() != MembershipType.SPECTATOR
                && request.getMembershipType() != MembershipType.PARTICIPANT) {
            throw new InvalidField("membership_type");
        }

        if(group.haveMember(request.getInvitedUserId())) {
            throw new AlreadyIn();
        }

        GroupMember invitor = gm.getGroupMember(invitorId, group.getId());
        if (invitor == null) {
            throw new NotFoundException("Group member not found.");
        }

        gi.createGroupInvitation(group, invitorId, request);
    }

    public void acceptInvitation(@NonNull Group group, @NonNull GroupInvitation invitation) {
        if (group.haveMember(invitation.getUserId())) {
            throw new AlreadyIn();
        }

        if (invitation.getMembershipType() != MembershipType.SPECTATOR
                && invitation.getMembershipType() != MembershipType.PARTICIPANT) {
            throw new InvalidField("membership_type");
        }

        if (group.getMembers().size() >= group.getMaxMember()) {
            throw new GroupReachLimit();
        }

        group.addMember(invitation.getUserId());
        query.save(group);

        listener.onAcceptInvitation(group, invitation);
    }

    public void rejectInvitation(@NonNull Group group, @NonNull GroupInvitation invitation) {
        listener.onRejectInvitation(group, invitation);
    }

    public Token generateToken(Group group) {
        List<Token> tokens = tokenService.get(ObjectType.GROUP, group.getId(), Action.INVITE);
        if (tokens.isEmpty()) {
            return tokenService.create(new TokenData(ObjectType.GROUP, group.getId(), Action.INVITE, Map.of(), null));
        }

        if (tokens.size() > 1) {
            throw new ServerException("Too many tokens");
        }

        return tokens.getFirst();
    }

    public void deactivateToken(Group group) {
        tokenService.delete(ObjectType.GROUP, group.getId(), Action.INVITE);
    }

    public Group getByToken(String uuid) {
        Token token = tokenService.get(uuid);
        if (token == null) {
            throw new NotFoundException("Group not found.");
        }

        if (token.getObjectType() != ObjectType.GROUP || token.getAction() != Action.INVITE) {
            throw new NotFoundException("Group not found.");
        }

        Group group = query.getById(token.getObjectId());
        if (group == null) {
            throw new NotFoundException("Group not found.");
        }

        return group;
    }

    public boolean haveMember(Long groupId, Long memberId) {
        Group group = query.getById(groupId);
        if (group == null) {
            return false;
        }

        return group.haveMember(memberId);
    }
}
