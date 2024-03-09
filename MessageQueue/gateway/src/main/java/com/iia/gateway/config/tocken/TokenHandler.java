package com.iia.gateway.config.tocken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import javax.naming.AuthenticationException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class TokenHandler {
    private final JwtHandler jwtHandler;
    private final String key;
    private final String ID;
    private final String ROLE_TYPES;
    private final String SEP;

    public Mono<PrivateClaims> parse(String token) {
        return Mono.fromCallable(() -> convert(jwtHandler.parse(key, token).orElseThrow()))
                .onErrorMap(ExpiredJwtException.class, e -> new ExpiredJwtException(null, null, "Token expired"))
                .onErrorMap(JwtException.class, e -> new AuthenticationException("Token invalid"));
    }

    private PrivateClaims convert(Claims claims) {
        return new PrivateClaims(
                claims.get(ID, String.class),
                Arrays.asList(claims.get(ROLE_TYPES, String.class).split(SEP))
        );
    }

    @Getter
    @AllArgsConstructor
    public static class PrivateClaims {
        private String id;
        private List<String> roleTypes;
    }
}