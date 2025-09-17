package com.example.learninghubbackend.services.group;

import com.example.learninghubbackend.models.group.Group;
import com.example.learninghubbackend.repositories.group.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupQuery {
    private final GroupRepository groupRepository;

    public void save(Group group) {
        groupRepository.save(group);
    }

    public Group getById(Long id) {
        return groupRepository.findById(id).orElse(null);
    }
}
