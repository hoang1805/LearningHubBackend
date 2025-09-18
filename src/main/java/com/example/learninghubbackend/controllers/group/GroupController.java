package com.example.learninghubbackend.controllers.group;

import com.example.learninghubbackend.commons.exceptions.CustomException;
import com.example.learninghubbackend.commons.exceptions.NotFoundException;
import com.example.learninghubbackend.commons.exceptions.NotHavePermission;
import com.example.learninghubbackend.dtos.requests.group.*;
import com.example.learninghubbackend.dtos.responses.BaseResponse;
import com.example.learninghubbackend.models.group.Group;
import com.example.learninghubbackend.services.AppContext;
import com.example.learninghubbackend.services.group.GroupACL;
import com.example.learninghubbackend.services.group.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/group/v1")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;
    private final GroupACL groupACL;
    private final AppContext appContext;

    @PostMapping("/create")
    public ResponseEntity<Object> createGroup(@RequestBody CreateGroupRequest request) {
        Long userId = appContext.getUserId();
        if (!groupACL.canCreate()) {
            throw new NotHavePermission("create group");
        }

        Group group = groupService.createGroup(userId, request);
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.success(group.release())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getGroup(@PathVariable("id") Long id) {
        Long userId = appContext.getUserId();
        Group group = groupService.query().getById(id);

        if (group == null) {
            throw new NotFoundException("Group not found");
        }

        if (!groupACL.canView(group)) {
            throw new NotHavePermission("view the group");
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.success(group.release())
        );
    }

    @PostMapping("/join")
    public ResponseEntity<Object> joinGroup(@RequestBody JoinRequest request) {
        Group group = groupService.query().getById(request.getGroupId());
        if (group == null) {
            throw new NotFoundException("Group not found");
        }

        if (!groupACL.canJoin(group)) {
            throw new NotHavePermission("join the group");
        }

        Long userId = appContext.getUserId();
        if (group.haveMember(userId)) {
            throw new CustomException("You have already been in this group", HttpStatus.BAD_REQUEST.value());
        }

        groupService.joinGroup(userId, request);
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.success()
        );
    }

    @PostMapping("/{id}/quit")
    public ResponseEntity<Object> quitGroup(@PathVariable("id") Long id) {
        Long userId = appContext.getUserId();
        Group group = groupService.query().getById(id);
        if (group == null) {
            throw new NotFoundException("Group not found");
        }

        if (!groupACL.canQuit(group)) {
            throw new NotHavePermission("quit the group");
        }

        groupService.quit(group, userId);

        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.success()
        );
    }

    @PostMapping("/{id}/kick")
    public ResponseEntity<Object> kickGroup(@PathVariable("id") Long id, @RequestBody KickRequest request) {
        Long userId = appContext.getUserId();
        Group group = groupService.query().getById(id);
        if (group == null) {
            throw new NotFoundException("Group not found");
        }

        if (!groupACL.canKick(group)) {
            throw new NotHavePermission("kick the group");
        }

        groupService.kick(group, userId, request.getKickedUserId());

        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.success()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateGroup(@PathVariable("id") Long id, @RequestBody ChangeInformationRequest request) {
        Group group = groupService.query().getById(id);
        if (group == null) {
            throw new NotFoundException("Group not found");
        }

        if (!groupACL.canEdit(group)) {
            throw new NotHavePermission("update the group");
        }

        groupService.updateGroup(group, request);
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.success(group.release())
        );
    }

    @PutMapping("/{id}/scope")
    public ResponseEntity<Object> changeScope(@PathVariable("id") Long id, @RequestBody ChangeScopeRequest request) {
        Group group = groupService.query().getById(id);
        if (group == null) {
            throw new NotFoundException("Group not found");
        }

        if (!groupACL.canEdit(group)) {
            throw new NotHavePermission("update the group");
        }

        groupService.updateGroup(group, request);
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.success(group.release())
        );
    }

    @PostMapping("/{id}/invite")
    public ResponseEntity<Object> inviteUser(@PathVariable("id") Long id, @RequestBody InviteRequest request) {
        Group group = groupService.query().getById(id);
        if (group == null) {
            throw new NotFoundException("Group not found");
        }

        if (!groupACL.canInvite(group)) {
            throw new NotHavePermission("invite member");
        }
        Long userId = appContext.getUserId();
        groupService.invite(group, userId, request);
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.success()
        );
    }

    @GetMapping("/token/{token}")
    public ResponseEntity<Object> getGroupByToken(@PathVariable("token") String token) {
        Group group = groupService.getByToken(token);
        if (group == null) {
            throw new NotFoundException("Group not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.success(group.release())
        );
    }

    @PostMapping("/join/token")
    public ResponseEntity<Object> joinByToken(@RequestBody JoinByToken request) {
        Group group = groupService.query().getById(request.getGroupId());
        if (group == null) {
            throw new NotFoundException("Group not found");
        }

        Long userId = appContext.getUserId();

        groupService.joinGroup(group, userId, request);
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.success()
        );
    }
}
