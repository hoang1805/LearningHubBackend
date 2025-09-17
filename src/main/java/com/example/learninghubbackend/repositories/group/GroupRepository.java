package com.example.learninghubbackend.repositories.group;

import com.example.learninghubbackend.models.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
}
