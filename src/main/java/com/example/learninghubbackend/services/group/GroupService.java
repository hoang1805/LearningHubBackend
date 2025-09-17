package com.example.learninghubbackend.services.group;

import com.example.learninghubbackend.commons.PropertiesData;
import com.example.learninghubbackend.dtos.requests.group.CreateGroupRequest;
import com.example.learninghubbackend.models.User;
import com.example.learninghubbackend.models.group.Group;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupQuery query;
    private final GroupListener listener;
    private final GroupReader reader;
    private final PropertiesData propertiesData;

    public GroupQuery query() {
        return query;
    }

    public GroupListener listener() {
        return listener;
    }

    public GroupReader reader() {
        return reader;
    }

    @Transactional
    public Group createGroup(Long userId, CreateGroupRequest request) {
        Group group = new Group();
        group.setMaxMember(propertiesData.getEnvironment().getProperty("app.group.limit", Integer.class));

        reader.read(group, request);

        group.addMember(userId);
        group.setCreatorId(userId);

        query.save(group);

        listener.onCreated(group);

        return group;
    }
}
