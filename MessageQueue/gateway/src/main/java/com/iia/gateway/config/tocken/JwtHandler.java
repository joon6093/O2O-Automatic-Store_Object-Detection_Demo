package com.iia.gateway.config.tocken;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.SignatureException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class JwtHandler {
    private String type = "Bearer ";

    public Optional<Claims> parse(String key, String token) {
        try {
            return Optional.of(Jwts.parser()
                    .setSigningKey(key.getBytes())
                    .parseClaimsJws(unType(token))
                    .getBody());
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token - {}", e.getMessage());
            throw e;
        } catch (JwtException e) {
            log.warn("JWT error - {}", e.getMessage());
            throw e;
        }
    }

    private String unType(String token) {
        if (token != null && token.startsWith(type)) {
            return token.substring(type.length());
        }
        return token;
    }
}