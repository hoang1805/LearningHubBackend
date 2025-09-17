package com.example.learninghubbackend.dtos.requests.user;

import com.example.learninghubbackend.services.user.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ChangeInformation {
    public String email;

    public String phone;

    public String name;

    public Gender gender;
}
