package com.iia.gateway.config.exception.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iia.gateway.config.exception.response.ResponseHandler;
import com.iia.gateway.config.response.Response;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;

import static com.iia.gateway.config.exception.type.ExceptionType.*;

@Component
@Order(-2)
public class CustomWebExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;
    private final ResponseHandler responseHandler;

    public CustomWebExceptionHandler(ObjectMapper objectMapper, ResponseHandler responseHandler) {
        this.objectMapper = objectMapper;
        this.responseHandler = responseHandler;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status;
        Response response;

        if (ex instanceof ExpiredJwtException) {
            status = HttpStatus.UNAUTHORIZED;
            response = responseHandler.getFailureResponse(EXPIRED_JWT_EXCEPTION);
        } else if (ex instanceof AuthenticationException) {
            status = HttpStatus.UNAUTHORIZED;
            response = responseHandler.getFailureResponse(AUTHENTICATION_ENTRY_POINT_EXCEPTION);
        } else if (ex instanceof AccessDeniedException) {
            status = HttpStatus.FORBIDDEN;
            response = responseHandler.getFailureResponse(ACCESS_DENIED_EXCEPTION);
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            response = responseHandler.getFailureResponse(EXCEPTION);
        }

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        byte[] bytes = toJson(response);

        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
    }

    private byte[] toJson(Response response) {
        try {
            return objectMapper.writeValueAsBytes(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}