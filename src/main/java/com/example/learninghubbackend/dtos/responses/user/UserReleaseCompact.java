package com.example.learninghubbackend.dtos.responses.user;

import com.example.learninghubbackend.services.user.Gender;
import com.example.learninghubbackend.services.user.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class UserReleaseCompact {
    private Long id;

    private String username;

    private String name;

    private String email;

    private Role role;
}
