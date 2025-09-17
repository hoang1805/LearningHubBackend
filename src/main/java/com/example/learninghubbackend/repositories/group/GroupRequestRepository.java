package com.example.learninghubbackend.repositories.group;

import com.example.learninghubbackend.models.group.GroupInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRequestRepository extends JpaRepository<GroupInvitation, Long> {
}
