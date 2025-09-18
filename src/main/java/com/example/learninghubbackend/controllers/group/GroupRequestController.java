package com.example.learninghubbackend.controllers.group;

import com.example.learninghubbackend.commons.exceptions.CustomException;
import com.example.learninghubbackend.commons.exceptions.NotFoundException;
import com.example.learninghubbackend.commons.exceptions.NotHavePermission;
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
@RequestMapping(path = "api/group/v1/request")
@RequiredArgsConstructor
public class GroupRequestController {
    private final GroupService groupService;
    private final GroupACL groupACL;
    private final AppContext appContext;
    private final GroupRequestService gs;

    @PostMapping("{id}/approved")
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

    @PostMapping("{id}/rejected")
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
}
