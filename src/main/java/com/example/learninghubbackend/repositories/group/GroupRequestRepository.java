package com.example.learninghubbackend.repositories.group;

import com.example.learninghubbackend.models.group.GroupInvitation;
import com.example.learninghubbackend.models.group.GroupRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRequestRepository extends JpaRepository<GroupRequest, Long> {
    Optional<GroupRequest> findByGroupIdAndUserId(Long groupId, Long userId);

    List<GroupRequest> findByGroupId(Long groupId);
}
