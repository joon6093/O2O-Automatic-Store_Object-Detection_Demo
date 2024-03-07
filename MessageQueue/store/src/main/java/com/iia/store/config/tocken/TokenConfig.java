package com.iia.store.config.tocken;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TokenConfig {

    private final JwtHandler jwtHandler;

    @Bean
    public TokenHandler accountAccessTokenHandler(
            @Value("${jwt.key.account.access}") String key,
            @Value("${jwt.max-age.account.access}") long maxAgeSeconds) {
        return new TokenHandler(jwtHandler, key, maxAgeSeconds);
    }

    @Bean
    public TokenHandler accountRefreshTokenHandler(
            @Value("${jwt.key.account.refresh}") String key,
            @Value("${jwt.max-age.account.refresh}") long maxAgeSeconds) {
        return new TokenHandler(jwtHandler, key, maxAgeSeconds);
    }

    @Bean
    public TokenHandler storeAccessTokenHandler(
            @Value("${jwt.key.store.access}") String key,
            @Value("${jwt.max-age.store.access}") long maxAgeSeconds) {
        return new TokenHandler(jwtHandler, key, maxAgeSeconds);
    }
}