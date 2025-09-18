package com.example.learninghubbackend.dtos.requests.group;

import com.example.learninghubbackend.services.group.MembershipType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class InviteRequest {
    @JsonProperty("user_id")
    private Long invitedUserId;

    @JsonProperty("membership_type")
    private MembershipType membershipType;
}
