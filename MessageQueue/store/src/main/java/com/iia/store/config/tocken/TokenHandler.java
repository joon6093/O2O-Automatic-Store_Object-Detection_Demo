package com.iia.store.config.tocken;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class TokenHandler {
    private final JwtHandler jwtHandler;
    private final String key;
    private final long maxAgeSeconds;
    private final String ID;
    private final String ROLE_TYPES;
    private final String SEP;

    public String createToken(PrivateClaims privateClaims) {
        return jwtHandler.createToken(
                key,
                Map.of(ID, privateClaims.getId(), ROLE_TYPES, String.join(SEP, privateClaims.getRoleTypes())),
                maxAgeSeconds
        );
    }

    public Optional<PrivateClaims> parse(String token) {
        return jwtHandler.parse(key, token).map(claims -> convert(claims));
    }

    public PrivateClaims convert(Claims claims) {
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