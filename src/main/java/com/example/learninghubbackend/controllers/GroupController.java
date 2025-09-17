package com.example.learninghubbackend.controllers;

import com.example.learninghubbackend.commons.exceptions.CustomException;
import com.example.learninghubbackend.commons.exceptions.NotFoundException;
import com.example.learninghubbackend.commons.exceptions.NotHavePermission;
import com.example.learninghubbackend.dtos.requests.group.CreateGroupRequest;
import com.example.learninghubbackend.dtos.requests.group.JoinRequest;
import com.example.learninghubbackend.dtos.requests.group.RejectRequest;
import com.example.learninghubbackend.dtos.responses.BaseResponse;
import com.example.learninghubbackend.models.group.Group;
import com.example.learninghubbackend.models.group.GroupRequest;
import com.example.learninghubbackend.services.AppContext;
import com.example.learninghubbackend.services.group.GroupACL;
import com.example.learninghubbackend.services.group.GroupService;
import com.example.learninghubbackend.services.group.request.GroupRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;
    private final GroupACL groupACL;
    private final AppContext appContext;
    private final GroupRequestService gs;

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
    
    @PostMapping("/request/{id}/approved")
    public ResponseEntity<Object> requestApproved(@PathVariable("id") Long id) {
        GroupRequest request = gs.getById(id);
        if (request == null) {
            throw new NotFoundException("Request not found");
        }

        Group group = groupService.query().getById(request.getGroupId());
        if (group == null) {
            throw new NotFoundException("Group not found");
        }

        if (!groupACL.canApprove(group)) {
            throw new NotHavePermission("approve the group");
        }

        Long userId = appContext.getUserId();
        if (userId.equals(request.getUserId())) {
            throw new CustomException("You have already been in this group", HttpStatus.BAD_REQUEST.value());
        }

        groupService.approveRequest(request, group);
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.success()
        );
    }

    @PostMapping("/request/{id}/rejected")
    public ResponseEntity<Object> requestRejected(@PathVariable("id") Long id, @RequestBody RejectRequest request) {
        GroupRequest groupRequest = gs.getById(id);
        if (groupRequest == null) {
            throw new NotFoundException("Request not found");
        }

        Group group = groupService.query().getById(groupRequest.getGroupId());
        if (group == null) {
            throw new NotFoundException("Group not found");
        }

        Long userId = appContext.getUserId();
        if (userId.equals(groupRequest.getUserId())) {
            throw new CustomException("You have already been in this group", HttpStatus.BAD_REQUEST.value());
        }

        groupService.rejectRequest(group, groupRequest, request);
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
}
