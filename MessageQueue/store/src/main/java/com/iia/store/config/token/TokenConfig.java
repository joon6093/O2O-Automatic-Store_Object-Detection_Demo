package com.iia.store.config.token;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TokenConfig {

    private final JwtHandler jwtHandler;
    private static final String MEMBER_ID = "MEMBER_ID";
    private static final String STORE_ID = "STORE_ID";
    private static final String ROLE_TYPES = "ROLE_TYPES";
    private static final String SEP = ",";

    @Bean
    public TokenHandler userAccessTokenHandler(
            @Value("${jwt.key.user.access}") String key,
            @Value("${jwt.max-age.account.access}") long maxAgeSeconds) {
        return new TokenHandler(jwtHandler, key, maxAgeSeconds, MEMBER_ID, ROLE_TYPES, SEP);
    }

    @Bean
    public TokenHandler userRefreshTokenHandler(
            @Value("${jwt.key.user.refresh}") String key,
            @Value("${jwt.max-age.account.refresh}") long maxAgeSeconds) {
        return new TokenHandler(jwtHandler, key, maxAgeSeconds, MEMBER_ID, ROLE_TYPES, SEP);
    }

    @Bean
    public TokenHandler storeAccessTokenHandler(
            @Value("${jwt.key.store.access}") String key,
            @Value("${jwt.max-age.store.access}") long maxAgeSeconds) {
        return new TokenHandler(jwtHandler, key, maxAgeSeconds, STORE_ID, ROLE_TYPES, SEP);
    }
}