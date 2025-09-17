package com.example.learninghubbackend.services.group.request;

import com.example.learninghubbackend.dtos.requests.group.JoinRequest;
import com.example.learninghubbackend.models.group.GroupRequest;
import com.example.learninghubbackend.repositories.group.GroupRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupRequestService {
    private final GroupRequestRepository groupRequestRepository;

    public GroupRequest getGroupRequest(Long groupId, Long userId) {
        return groupRequestRepository.findByGroupIdAndUserId(groupId, userId).orElse(null);
    }

    public GroupRequest createGroupRequest(Long userId, JoinRequest request) {
        GroupRequest groupRequest = getGroupRequest(userId, request.getGroupId());
        if (groupRequest == null) {
            groupRequest = new GroupRequest();
        }

        groupRequest.setUserId(userId);
        groupRequest.setMembershipType(request.getMembershipType());
        groupRequest.setGroupId(request.getGroupId());
        groupRequest.setMessage(request.getMessage());

        return groupRequestRepository.save(groupRequest);
    }

    public List<GroupRequest> getByGroup(Long groupId) {
        return groupRequestRepository.findByGroupId(groupId);
    }

    public GroupRequest getById(Long id) {
        return groupRequestRepository.findById(id).orElse(null);
    }

    public void remove(GroupRequest groupRequest) {
        groupRequestRepository.delete(groupRequest);
    }

    public void remove(Long groupId, Long userId) {
        GroupRequest groupRequest = getById(groupId);
        if (groupRequest == null) {
            return;
        }

        groupRequestRepository.delete(groupRequest);
    }
}
