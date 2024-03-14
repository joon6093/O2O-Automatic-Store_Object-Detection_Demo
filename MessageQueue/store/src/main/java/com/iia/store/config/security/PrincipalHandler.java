package com.iia.store.config.security;

import com.iia.store.entity.role.RoleType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PrincipalHandler {

    public static Long extractId() {
        return Long.valueOf(getUserDetails().getUsername());
    }

    public static Set<RoleType> extractRoles() {
        return getUserDetails().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(RoleType::valueOf)
                .collect(Collectors.toSet());
    }

    private static CustomUserDetails getUserDetails() {
        return (CustomUserDetails) getAuthentication().getPrincipal();
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}