package com.example.learninghubbackend.services.group;

import com.example.learninghubbackend.models.User;
import com.example.learninghubbackend.models.group.Group;
import com.example.learninghubbackend.models.group.GroupMember;
import com.example.learninghubbackend.services.AppContext;
import com.example.learninghubbackend.services.group.member.GroupMemberService;
import com.example.learninghubbackend.services.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupACL implements IGroupACL {
    private final GroupService groupService;
    private final GroupMemberService gm;
    private final AppContext appContext;

    @Override
    public boolean canCreateCode(User user, Group group) {
        return false;
    }

    @Override
    public boolean canInvite(Group group) {
        if (group.getRegistrationPolicy() == RegistrationPolicy.REQUEST_APPROVAL) {
            return false;
        }

        Long userId = appContext.getUserId();
        GroupMember member = gm.getGroupMember(userId, group.getId());
        if (member == null) {
            return false;
        }

        return member.isCreator() || member.isManager();
    }

    @Override
    public boolean canJoin(Group group) {
        return group.getScope() != Scope.PRIVATE;
    }

    @Override
    public boolean canApprove(Group group) {
        if (group.getScope() == Scope.PRIVATE) {
            return false;
        }

        Long userId = appContext.getUserId();
        GroupMember member = gm.getGroupMember(userId, group.getId());
        if (member == null) {
            return false;
        }

        return member.isCreator() || member.isManager();
    }

    @Override
    public boolean canQuit(Group group) {
        Long userId = appContext.getUserId();
        return group.haveMember(userId);
    }

    @Override
    public boolean canKick(Group group) {
        Long userId = appContext.getUserId();
        GroupMember member = gm.getGroupMember(userId, group.getId());
        if (member == null) {
            return false;
        }

        return member.isCreator() || member.isManager();
    }

    @Override
    public boolean canCreate() {
        return true;
    }

    @Override
    public boolean canDelete(Group object) {
        return canEdit(object);
    }

    @Override
    public boolean canEdit(Group object) {
        Long userId = appContext.getUserId();
        if (!object.haveMember(userId)) {
            return false;
        }

        GroupMember member = gm.getGroupMember(userId, object.getId());
        if (member == null) {
            return false;
        }

        return member.isCreator();
    }

    @Override
    public boolean canView(Group object) {
        Long userId = appContext.getUserId();
        if (appContext.hasAnyRole(Role.ADMIN.toString(), Role.OWNER.toString())) {
            return true;
        }

        return object.haveMember(userId);
    }
}
