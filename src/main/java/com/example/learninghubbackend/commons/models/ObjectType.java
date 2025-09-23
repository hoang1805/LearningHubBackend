package com.example.learninghubbackend.commons.models;

import lombok.Getter;

@Getter
public enum ObjectType {
    USER("app.user"),

    SESSION("app.session"),

    GROUP("app.group"),
    GROUP_MEMBER("app.group.member"),
    GROUP_INVITATION("app.group.invitation"),

    TOKEN("app.token"),

    POST("app.post"),
    COMMENT("app.comment"),
    VOTE("app.vote"),
    ;

    private final String value;
    ObjectType(String s) {
        value = s;
    }
}
