package com.example.learninghubbackend.services.group;

import com.example.learninghubbackend.commons.PropertiesData;
import com.example.learninghubbackend.commons.exceptions.AlreadyExists;
import com.example.learninghubbackend.commons.exceptions.CustomException;
import com.example.learninghubbackend.commons.exceptions.InvalidField;
import com.example.learninghubbackend.commons.exceptions.NotFoundException;
import com.example.learninghubbackend.dtos.requests.group.CreateGroupRequest;
import com.example.learninghubbackend.dtos.requests.group.JoinRequest;
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
            throw new CustomException("You have already in this group.", HttpStatus.BAD_REQUEST.value());
        }

        if (group.getMembers().size() >= group.getMaxMember()) {
            throw new CustomException("You can not join this group because it reaches the limit members.", HttpStatus.BAD_REQUEST.value());
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
}
