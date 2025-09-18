package com.example.learninghubbackend.controllers.group;

import com.example.learninghubbackend.commons.exceptions.NotFoundException;
import com.example.learninghubbackend.dtos.responses.BaseResponse;
import com.example.learninghubbackend.models.group.Group;
import com.example.learninghubbackend.models.group.GroupInvitation;
import com.example.learninghubbackend.services.AppContext;
import com.example.learninghubbackend.services.group.GroupACL;
import com.example.learninghubbackend.services.group.GroupService;
import com.example.learninghubbackend.services.group.invitation.GroupInvitationService;
import com.example.learninghubbackend.services.group.request.GroupRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/group/v1/invitation")
@RequiredArgsConstructor
public class GroupInviteController {
    private final GroupService groupService;
    private final GroupACL groupACL;
    private final AppContext appContext;
    private final GroupInvitationService gi;

    @PostMapping("/{id}/accepted")
    public ResponseEntity<Object> acceptInvitation(@PathVariable("id") long id) {
        GroupInvitation invitation = gi.getById(id);
        if (invitation == null) {
            throw new NotFoundException("Invitation not found.");
        }

        Long userId = appContext.getUserId();
        if (!userId.equals(invitation.getUserId())) {
            throw new NotFoundException("Invitation not found.");
        }

        Group group = groupService.query().getById(invitation.getGroupId());

        groupService.acceptInvitation(group, invitation);
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.success()
        );
    }

    @PostMapping("/{id}/rejected")
    public ResponseEntity<Object> rejectInvitation(@PathVariable("id") long id) {
        GroupInvitation invitation = gi.getById(id);
        if (invitation == null) {
            throw new NotFoundException("Invitation not found.");
        }

        Long userId = appContext.getUserId();
        if (!userId.equals(invitation.getUserId())) {
            throw new NotFoundException("Invitation not found.");
        }

        Group group = groupService.query().getById(invitation.getGroupId());
        if (group == null) {
            throw new NotFoundException("Group not found.");
        }

        groupService.rejectInvitation(group, invitation);
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.success()
        );
    }
}
