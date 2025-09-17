package com.example.learninghubbackend.services.group;

import com.example.learninghubbackend.commons.PropertiesData;
import com.example.learninghubbackend.commons.exceptions.*;
import com.example.learninghubbackend.commons.exceptions.group.AlreadyIn;
import com.example.learninghubbackend.commons.exceptions.group.GroupReachLimit;
import com.example.learninghubbackend.dtos.requests.group.CreateGroupRequest;
import com.example.learninghubbackend.dtos.requests.group.JoinRequest;
import com.example.learninghubbackend.dtos.requests.group.RejectRequest;
import com.example.learninghubbackend.models.User;
import com.example.learninghubbackend.models.group.Group;
import com.example.learninghubbackend.models.group.GroupRequest;
import com.example.learninghubbackend.services.group.request.GroupRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupQuery query;
    private final GroupListener listener;
    private final GroupReader reader;
    private final PropertiesData propertiesData;
    private final GroupRequestService gs;

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

        listener.onCreated(group);

        return group;
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
        GroupRequest groupRequest = gs.createGroupRequest(userId, request);
    }

    @Transactional
    public void approveRequest(GroupRequest request, Group group) {
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

        group.addMember(request.getUserId());
        query.save(group);

        listener.onApproved(group, request);
    }

    @Transactional
    public void rejectRequest(Group group, GroupRequest groupRequest, RejectRequest request) {
        listener.onRejected(group, groupRequest, request);
    }
}
