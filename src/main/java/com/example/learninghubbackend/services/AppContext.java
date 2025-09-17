package com.example.learninghubbackend.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppContext {
    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Long getUserId() {
        return (Long) getAuth().getPrincipal();
    }

    public List<String> getRoles() {
        return getAuth().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }

    public boolean hasRole(String role) {
        return getRoles().contains(String.format("ROLE_%s", role));
    }

    public boolean hasAnyRole(String... roles) {
        List<String> userRoles = getRoles();
        return Arrays.stream(roles).map(r -> String.format("ROLE_%s", r)).anyMatch(userRoles::contains);
    }
}
