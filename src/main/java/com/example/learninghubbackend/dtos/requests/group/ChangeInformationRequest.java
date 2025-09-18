package com.example.learninghubbackend.dtos.requests.group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ChangeInformationRequest {
    private String name;
    private String description;
}
