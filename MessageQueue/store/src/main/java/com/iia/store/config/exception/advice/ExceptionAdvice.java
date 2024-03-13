package com.iia.store.config.exception.advice;

import com.iia.store.config.exception.*;
import com.iia.store.config.response.Response;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.iia.store.config.exception.response.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.iia.store.config.exception.type.ExceptionType.*;
import static com.iia.store.config.exception.type.ExceptionType.IMAGE_DELETE_FAILURE_EXCEPTION;


@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ExceptionAdvice {

    private final ResponseHandler responseHandler;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> exception(Exception e) {
        log.info("e = {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseHandler.getFailureResponse(EXCEPTION));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseHandler.getFailureResponse(BIND_EXCEPTION, e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

    @ExceptionHandler(LoginFailureException.class)
    public ResponseEntity<Response> loginFailureException() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(responseHandler.getFailureResponse(LOGIN_FAILURE_EXCEPTION));
    }

    @ExceptionHandler(MemberEmailAlreadyExistsException.class)
    public ResponseEntity<Response> memberEmailAlreadyExistsException(MemberEmailAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(responseHandler.getFailureResponse(MEMBER_EMAIL_ALREADY_EXISTS_EXCEPTION, e.getMessage()));
    }

    @ExceptionHandler(MemberNicknameAlreadyExistsException.class)
    public ResponseEntity<Response> memberNicknameAlreadyExistsException(MemberNicknameAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(responseHandler.getFailureResponse(MEMBER_NICKNAME_ALREADY_EXISTS_EXCEPTION, e.getMessage()));
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<Response> memberNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseHandler.getFailureResponse(MEMBER_NOT_FOUND_EXCEPTION));
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Response> roleNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseHandler.getFailureResponse(ROLE_NOT_FOUND_EXCEPTION));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Response> missingRequestHeaderException(MissingRequestHeaderException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseHandler.getFailureResponse(MISSING_REQUEST_HEADER_EXCEPTION, e.getHeaderName()));
    }

    @ExceptionHandler(ExpiredJwtException.class) // refreshTokenHelper.parse(rToken)으로 부터 발생하는 오류
    public ResponseEntity<Response> handleExpiredJwtException(ExpiredJwtException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(responseHandler.getFailureResponse(EXPIRED_JWT_EXCEPTION));
    }

    @ExceptionHandler(UnsupportedImageFormatException.class)
    public ResponseEntity<Response> unsupportedImageFormatException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseHandler.getFailureResponse(UNSUPPORTED_IMAGE_FORMAT_EXCEPTION));
    }

    @ExceptionHandler(ImageUploadFailureException.class)
    public ResponseEntity<Response> imageUploadFailureException(ImageUploadFailureException e) {
        log.info("e = {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseHandler.getFailureResponse(IMAGE_UPLOAD_FAILURE_EXCEPTION));
    }

    @ExceptionHandler(ImageDeleteFailureException.class)
    public ResponseEntity<Response> imageDeleteFailureException() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseHandler.getFailureResponse(IMAGE_DELETE_FAILURE_EXCEPTION));
    }

    @ExceptionHandler(RefreshTokenFailureException.class)
    public ResponseEntity<Response> refreshTokenFailureException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseHandler.getFailureResponse(REFRESH_TOKEN_FAILURE_EXCEPTION));
    }

    @ExceptionHandler(StoreNotFoundException.class)
    public ResponseEntity<Response> storeNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseHandler.getFailureResponse(STORE_NOT_FOUND_EXCEPTION));
    }

    @ExceptionHandler(SelectStoreFailureException.class)
    public ResponseEntity<Response> selectStoreFailureException() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(responseHandler.getFailureResponse(SELECT_STORE_FAILURE_EXCEPTION));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Response> productNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseHandler.getFailureResponse(PRODUCT_NOT_FOUND_EXCEPTION));
    }

    @ExceptionHandler(RefreshUserTokenNotFoundException.class)
    public ResponseEntity<Response> refreshUserTokenNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseHandler.getFailureResponse(REFRESH_USER_TOKEN_NOT_FOUND_EXCEPTION));
    }
}