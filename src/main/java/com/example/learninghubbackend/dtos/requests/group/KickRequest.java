package com.example.learninghubbackend.dtos.requests.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class KickRequest {
    @JsonProperty("user_id")
    private Long kickedUserId;
}
