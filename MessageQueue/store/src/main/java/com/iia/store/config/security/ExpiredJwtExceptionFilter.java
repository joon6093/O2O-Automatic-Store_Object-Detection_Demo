package com.iia.store.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iia.store.config.exception.response.ResponseHandler;
import com.iia.store.config.response.Response;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.iia.store.config.exception.type.ExceptionType.EXPIRED_JWT_EXCEPTION;

@RequiredArgsConstructor
@Component
public class ExpiredJwtExceptionFilter extends OncePerRequestFilter {

    private final ResponseHandler responseHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            handleExpiredJwtException(response, e);
        }
    }

    private void handleExpiredJwtException(HttpServletResponse response, ExpiredJwtException e) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(convertToJson(responseHandler.getFailureResponse(EXPIRED_JWT_EXCEPTION)));
    }

    private String convertToJson(Response response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(response);
    }
}