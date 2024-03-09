package com.iia.gateway.config.tocken;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TokenConfig {

    private final JwtHandler jwtHandler;
    private static final String STORE_ID = "STORE_ID";
    private static final String ROLE_TYPES = "ROLE_TYPES";
    private static final String SEP = ",";

    @Bean
    public TokenHandler storeAccessTokenHandler(
            @Value("${jwt.key.store.access}") String key){
        return new TokenHandler(jwtHandler, key, STORE_ID, ROLE_TYPES, SEP);
    }
}