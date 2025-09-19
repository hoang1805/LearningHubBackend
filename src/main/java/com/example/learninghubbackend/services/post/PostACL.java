package com.example.learninghubbackend.services.post;

import com.example.learninghubbackend.models.group.Group;
import com.example.learninghubbackend.models.group.GroupMember;
import com.example.learninghubbackend.models.post.Post;
import com.example.learninghubbackend.services.AppContext;
import com.example.learninghubbackend.services.group.GroupService;
import com.example.learninghubbackend.services.group.MembershipType;
import com.example.learninghubbackend.services.group.member.GroupMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostACL implements IPostACL {
    private final AppContext appContext;
    private final GroupService groupService;
    private final GroupMemberService gm;

    @Override
    public boolean canCreate() {
        return false;
    }

    @Override
    public boolean canDelete(Post object) {
        Long userId = appContext.getUserId();
        if (object.getScope() == Scope.PUBLIC) {
            return object.getCreatorId().equals(userId);
        }

        Group group = groupService.query().getById(object.getGroupId());
        if (group == null) {
            return false;
        }

        return group.haveMember(userId) || group.getCreatorId().equals(userId);
    }

    @Override
    public boolean canEdit(Post object) {
        Long userId = appContext.getUserId();
        if (object.getScope() == Scope.PUBLIC) {
            return object.getCreatorId().equals(userId);
        }

        Group group = groupService.query().getById(object.getGroupId());
        if (group == null) {
            return false;
        }

        return group.haveMember(userId);
    }

    @Override
    public boolean canView(Post object) {
        if (object.getScope() == Scope.PUBLIC) {
            return true;
        }

        Group group = groupService.query().getById(object.getGroupId());
        if (group == null) {
            return false;
        }

        Long userId = appContext.getUserId();
        return group.haveMember(userId);
    }

    @Override
    public boolean canCreate(Scope scope, Long groupId) {
        if (scope == Scope.PUBLIC) {
            return true;
        }

        Group group = groupService.query().getById(groupId);
        if (group == null) {
            return false;
        }

        Long userId = appContext.getUserId();
        if (!group.haveMember(userId)) {
            return false;
        }

        GroupMember member = gm.getGroupMember(userId, groupId);
        if (member == null) {
            return false;
        }

        if (member.getMembershipType() == MembershipType.SPECTATOR) {
            return false;
        }

        return true;
    }

    @Override
    public boolean canComment(Post post) {
        if (post.getScope() == Scope.PUBLIC) {
            return true;
        }

        Group group = groupService.query().getById(post.getGroupId());
        if (group == null) {
            return false;
        }

        Long userId = appContext.getUserId();
        if (!group.haveMember(userId)) {
            return false;
        }

        GroupMember member = gm.getGroupMember(userId, group.getId());
        if (member == null) {
            return false;
        }

        if (member.getMembershipType() == MembershipType.SPECTATOR) {
            return false;
        }

        return true;
    }

    @Override
    public boolean canVote(Post post) {
        return canComment(post);
    }
}
