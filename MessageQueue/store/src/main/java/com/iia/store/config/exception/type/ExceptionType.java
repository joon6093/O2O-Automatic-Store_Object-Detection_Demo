package com.iia.store.config.exception.type;

import lombok.Getter;

@Getter
public enum ExceptionType {
    EXCEPTION("exception.code", "exception.msg"),
    AUTHENTICATION_ENTRY_POINT_EXCEPTION("authenticationEntryPointException.code", "authenticationEntryPointException.msg"),
    ACCESS_DENIED_EXCEPTION("accessDeniedException.code", "accessDeniedException.msg"),
    BIND_EXCEPTION("bindException.code", "bindException.msg"),
    LOGIN_FAILURE_EXCEPTION("loginFailureException.code", "loginFailureException.msg"),
    MEMBER_EMAIL_ALREADY_EXISTS_EXCEPTION("memberEmailAlreadyExistsException.code", "memberEmailAlreadyExistsException.msg"),
    MEMBER_NICKNAME_ALREADY_EXISTS_EXCEPTION("memberNicknameAlreadyExistsException.code", "memberNicknameAlreadyExistsException.msg"),
    MEMBER_NOT_FOUND_EXCEPTION("memberNotFoundException.code", "memberNotFoundException.msg"),
    ROLE_NOT_FOUND_EXCEPTION("roleNotFoundException.code", "roleNotFoundException.msg"),
    MISSING_REQUEST_HEADER_EXCEPTION("missingRequestHeaderException.code", "missingRequestHeaderException.msg"),
    EXPIRED_JWT_EXCEPTION("expiredJwtException.code", "expiredJwtException.msg"),
    UNSUPPORTED_IMAGE_FORMAT_EXCEPTION("unsupportedImageFormatException.code", "unsupportedImageFormatException.msg"),
    IMAGE_UPLOAD_FAILURE_EXCEPTION("imageUploadFailureException.code", "imageUploadFailureException.msg"),
    IMAGE_DELETE_FAILURE_EXCEPTION("imageDeleteFailureException.code", "imageDeleteFailureException.msg"),
    REFRESH_TOKEN_FAILURE_EXCEPTION("refreshTokenFailureException.code", "refreshTokenFailureException.msg"),
    STORE_NOT_FOUND_EXCEPTION("storeNotFoundException.code", "storeNotFoundException.msg"),
    SELECT_STORE_FAILURE_EXCEPTION("selectStoreFailureException.code", "selectStoreFailureException.msg"),
    PRODUCT_NOT_FOUND_EXCEPTION("productNotFoundException.code", "productNotFoundException.msg");

    private final String code;
    private final String message;

    ExceptionType(String code, String message) {
        this.code = code;
        this.message = message;
    }
}