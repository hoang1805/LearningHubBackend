package com.example.learninghubbackend.services.group;

import com.example.learninghubbackend.commons.IBaseACL;
import com.example.learninghubbackend.models.User;
import com.example.learninghubbackend.models.group.Group;

public interface IGroupACL extends IBaseACL<Group> {
    boolean canCreateCode(User user, Group group);

    boolean canInvite(Group group);

    boolean canJoin(Group group);

    boolean canApprove(Group group);

    boolean canQuit(Group group);

    boolean canKick(Group group);
}
