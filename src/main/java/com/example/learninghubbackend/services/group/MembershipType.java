package com.example.learninghubbackend.services.group;

public enum MembershipType {
    SPECTATOR, PARTICIPANT, MANAGER, CREATOR ;

    public static MembershipType fromString(String type) {
        try {
            return valueOf(type.toUpperCase());
        } catch (Exception e) {
            return SPECTATOR;
        }
    }
}
