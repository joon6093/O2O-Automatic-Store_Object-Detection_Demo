package com.iia.gateway.filter;

import com.iia.gateway.config.tocken.TokenHandler;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Component
public class JwtStoreAuthenticationFilter extends AbstractGatewayFilterFactory<JwtStoreAuthenticationFilter.Config> {

    private final TokenHandler storeAccessTokenHandler;

    public JwtStoreAuthenticationFilter(TokenHandler storeAccessTokenHandler) {
        super(Config.class);
        this.storeAccessTokenHandler = storeAccessTokenHandler;
    }

    public static class Config {

    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> extractToken(exchange)
                .map(token -> storeAccessTokenHandler.parse(token)
                        .flatMap(claims -> {
                            String storeId = claims.getId();
                            exchange.getRequest().mutate().header("Store-ID", storeId).build();
                            return chain.filter(exchange);
                        })
                        .onErrorResume(e -> {
                            if (e instanceof ExpiredJwtException) {
                                return Mono.error(new ExpiredJwtException(null, null, "Token expired"));
                            } else {
                                return Mono.error(new AuthenticationException("Invalid token"));
                            }
                        }))
                .orElseGet(() -> Mono.error(new AuthenticationException("Authorization header missing")));
    }

    private Optional<String> extractToken(ServerWebExchange exchange) {
        return Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("Authorization"))
                .filter(authHeader -> authHeader.startsWith("Bearer "));
    }
}
