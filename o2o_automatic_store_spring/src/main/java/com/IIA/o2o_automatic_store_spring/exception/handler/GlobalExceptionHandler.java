package com.IIA.o2o_automatic_store_spring.exception.handler;

import com.IIA.o2o_automatic_store_spring.dto.response.Response;
import com.IIA.o2o_automatic_store_spring.exception.ImageReadException;
import com.IIA.o2o_automatic_store_spring.exception.ImageUploadFailureException;
import com.IIA.o2o_automatic_store_spring.exception.NullResponseFromApiException;
import com.IIA.o2o_automatic_store_spring.exception.SnackNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.net.BindException;

import static com.IIA.o2o_automatic_store_spring.exception.type.ExceptionType.*;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final ResponseHandler responseHandler;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> exception(Exception e) {
        log.info("e = {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseHandler.getFailureResponse(EXCEPTION));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Response> bindException(BindException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseHandler.getFailureResponse(BIND_EXCEPTION, e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseHandler.getFailureResponse(BIND_EXCEPTION, e.getMessage()));
    }
    @ExceptionHandler(ImageUploadFailureException.class)
    public ResponseEntity<Response> fileUploadFailureException(ImageUploadFailureException e) {
        log.info("e = {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseHandler.getFailureResponse(IMAGE_UPLOAD_FAILURE_EXCEPTION));
    }
    @ExceptionHandler(SnackNotFoundException.class)
    public ResponseEntity<Response> snackNotFoundException(SnackNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseHandler.getFailureResponse(SNACK_NOT_FOUND_EXCEPTION, e.getMessage()));
    }

    @ExceptionHandler(ImageReadException.class)
    public ResponseEntity<Response> imageReadException(ImageReadException e) {
        log.info("e = {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseHandler.getFailureResponse(IMAGE_READ_EXCEPTION));
    }

    @ExceptionHandler(NullResponseFromApiException.class)
    public ResponseEntity<Response> nullResponseFromApiException() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseHandler.getFailureResponse(NULL_RESPONSE_FROM_API_EXCEPTION));
    }
}
