package com.iia.store.controller.sign;

import com.iia.store.config.aop.AssignMemberId;
import com.iia.store.config.response.Response;
import com.iia.store.dto.sign.SignInRequest;
import com.iia.store.dto.sign.SignOutRequest;
import com.iia.store.dto.sign.SignUpRequest;
import com.iia.store.service.sign.SignService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignController {
    private final SignService signService;

    @PostMapping("/sign-up")
    public ResponseEntity<Response> signUp(@Valid @RequestBody SignUpRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.success(signService.signUp(req)));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Response> signIn(@Valid @RequestBody SignInRequest req, HttpServletResponse response) {
        signService.signIn(req, response);
        return ResponseEntity.status(HttpStatus.OK).body(Response.success());
    }

    @AssignMemberId
    @PostMapping("/sign-out")
    public ResponseEntity<Response> signOut(@Valid SignOutRequest req) {
        signService.signOut(req);
        return ResponseEntity.status(HttpStatus.OK).body(Response.success());
    }

    @PostMapping("/refresh")
    public ResponseEntity<Response> refresh(@RequestHeader(value = "Authorization") String refreshToken, HttpServletResponse response) {
        signService.refresh(refreshToken, response);
        return ResponseEntity.status(HttpStatus.OK).body(Response.success());
    }
}