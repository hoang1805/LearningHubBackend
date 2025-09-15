package com.example.learninghubbackend.dtos.responses.user;

import com.example.learninghubbackend.services.user.Gender;
import com.example.learninghubbackend.services.user.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class UserRelease {
    private Long id;

    private String username;

    private String name;

    private String email;

    private String phone;

    private Role role;

    private Gender gender;

    @JsonProperty("creator_id")
    private Long creatorId;

    @JsonProperty("created_at")
    private Long createdAt;

    @JsonProperty("updated_at")
    private Long updatedAt;
}
