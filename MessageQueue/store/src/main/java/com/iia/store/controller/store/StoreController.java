package com.iia.store.controller.store;

import com.iia.store.config.aop.AssignMemberId;
import com.iia.store.config.response.Response;
import com.iia.store.dto.store.*;
import com.iia.store.service.store.StoreService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @AssignMemberId
    @PostMapping("/stores")
    public ResponseEntity<Response> create(@Valid @ModelAttribute  StoreCreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.success(storeService.create(req)));
    }

    @DeleteMapping("/stores/{id}")
    public ResponseEntity<Response> delete(@PathVariable(name = "id") Long id) {
        storeService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(Response.success());
    }

    @AssignMemberId
    @GetMapping("/stores/sign-in")
    public ResponseEntity<Response> readBySignIn(@Valid StoreReadBySignInrRequest req) {
        StoreListDto stores = storeService.readBySignIn(req.getMemberId());
        return ResponseEntity.status(HttpStatus.OK).body(Response.success(stores));
    }

    @AssignMemberId
    @PostMapping("/stores/select")
    public ResponseEntity<Response> storeSelect(@Valid @RequestBody StoreSelectRequest req) {
        StoreSelectResponse response = storeService.storeSelect(req);
        return ResponseEntity.status(HttpStatus.OK).body(Response.success(response));
    }

    @PostMapping("/stores/refresh")
    public ResponseEntity<Response> storeRefresh(@Valid @RequestBody StoreRefreshTokenRequest req, HttpServletResponse response) {
        storeService.storeRefresh(req, response);
        return ResponseEntity.status(HttpStatus.OK).body(Response.success());
    }

    @GetMapping("/stores")
    public ResponseEntity<Response> readAll() {
        StoreListDto stores = storeService.readAll();
        return ResponseEntity.status(HttpStatus.OK).body(Response.success(stores));
    }

    @GetMapping("/stores/{id}")
    public ResponseEntity<Response> read(@PathVariable(name = "id") Long id) {
        StoreDto store = storeService.read(id);
        return ResponseEntity.status(HttpStatus.OK).body(Response.success(store));
    }
}