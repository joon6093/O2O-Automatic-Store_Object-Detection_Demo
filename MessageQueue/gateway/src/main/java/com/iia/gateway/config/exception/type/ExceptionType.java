package com.iia.gateway.config.exception.type;

import lombok.Getter;

@Getter
public enum ExceptionType {
    EXCEPTION("exception.code", "exception.msg"),
    AUTHENTICATION_ENTRY_POINT_EXCEPTION("authenticationEntryPointException.code", "authenticationEntryPointException.msg"),
    ACCESS_DENIED_EXCEPTION("accessDeniedException.code", "accessDeniedException.msg"),
    EXPIRED_JWT_EXCEPTION("expiredJwtException.code", "expiredJwtException.msg");


    private final String code;
    private final String message;

    ExceptionType(String code, String message) {
        this.code = code;
        this.message = message;
    }
}