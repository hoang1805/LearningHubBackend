package com.example.learninghubbackend.dtos.requests.vote;

import com.example.learninghubbackend.services.vote.VoteType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoteRequest {
    private VoteType vote;
}
