package com.iia.store.config.security;

import com.iia.store.config.token.TokenHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final TokenHandler userAccessTokenHandler;

    public CustomUserDetailsService(@Qualifier("userAccessTokenHandler") TokenHandler userAccessTokenHandler) {
        this.userAccessTokenHandler = userAccessTokenHandler;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        CustomUserDetails customUserDetails = userAccessTokenHandler.parse(token)
                .map(privateClaims -> convert(privateClaims))
                .orElse(null);
        return customUserDetails;
    }

    private CustomUserDetails convert(TokenHandler.PrivateClaims privateClaims) {
        return new CustomUserDetails(
                privateClaims.getId(),
                privateClaims.getRoleTypes().stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toSet())
        );
    }
}