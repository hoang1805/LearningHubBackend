package com.example.learninghubbackend.controllers;

import com.example.learninghubbackend.commons.exceptions.CustomException;
import com.example.learninghubbackend.commons.exceptions.NotFoundException;
import com.example.learninghubbackend.commons.exceptions.NotHavePermission;
import com.example.learninghubbackend.dtos.requests.group.CreateGroupRequest;
import com.example.learninghubbackend.dtos.requests.group.JoinRequest;
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
@RequestMapping(path = "api/group")
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
}
