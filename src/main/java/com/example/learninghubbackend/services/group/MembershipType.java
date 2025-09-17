package com.example.learninghubbackend.services.group;

public enum MembershipType {
    CREATOR, MANAGER, PARTICIPANT, SPECTATOR;

    public static MembershipType fromString(String type) {
        try {
            return valueOf(type.toUpperCase());
        } catch (Exception e) {
            return SPECTATOR;
        }
    }
}
