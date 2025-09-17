package com.example.learninghubbackend.services.group.member;

import com.example.learninghubbackend.models.group.GroupMember;
import com.example.learninghubbackend.repositories.group.GroupMemberRepository;
import com.example.learninghubbackend.services.group.MembershipType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupMemberService {
    private final GroupMemberRepository groupMemberRepository;

    public GroupMember getGroupMember(Long userId, Long groupId) {
        return groupMemberRepository.findByGroupIdAndUserId(groupId, userId).orElse(null);
    }

    public GroupMember createGroupMember(Long userId, Long groupId, MembershipType membershipType) {
        GroupMember groupMember = new GroupMember(userId, groupId, membershipType);
        return groupMemberRepository.save(groupMember);
    }
}
