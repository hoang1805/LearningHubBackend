package com.example.learninghubbackend.dtos.requests.group;

import com.example.learninghubbackend.services.group.RegistrationPolicy;
import com.example.learninghubbackend.services.group.Scope;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreateGroupRequest {
    private String name;

    private String description;

    private Scope scope;

    @JsonProperty("registration_policy")
    private RegistrationPolicy registrationPolicy;
}
