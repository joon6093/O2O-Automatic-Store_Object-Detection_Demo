package com.iia.store.config.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class CustomAuthenticationToken extends AbstractAuthenticationToken {

    private final CustomUserDetails principal;

    public CustomAuthenticationToken(CustomUserDetails principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public CustomUserDetails getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        throw new UnsupportedOperationException();
    }

}