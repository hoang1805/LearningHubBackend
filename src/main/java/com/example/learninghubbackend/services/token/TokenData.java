package com.example.learninghubbackend.services.token;

import com.example.learninghubbackend.commons.models.ObjectType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class TokenData {
    private ObjectType objectType;

    private Long objectId;

    private Action action;

    private Map<String, Object> data;

    private Long expiredAt;
}
