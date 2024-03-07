package com.iia.store.controller.store;

import com.iia.store.config.aop.AssignMemberId;
import com.iia.store.config.response.Response;
import com.iia.store.dto.store.StoreCreateRequest;
import com.iia.store.dto.store.StoreDto;
import com.iia.store.dto.store.StoreListDto;
import com.iia.store.service.store.StoreService;
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
    public ResponseEntity<Response> create(@Valid @RequestBody StoreCreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.success(storeService.create(req)));
    }

    @DeleteMapping("/stores/{id}")
    public ResponseEntity<Response> delete(@PathVariable(name = "id") Long id) {
        storeService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(Response.success());
    }

    @GetMapping("/stores/member/{id}")
    public ResponseEntity<Response> readByMember(@PathVariable(name = "id") Long id) {
        StoreListDto stores = storeService.readByMember(id);
        return ResponseEntity.ok(Response.success(stores));
    }

    @GetMapping("/stores")
    public ResponseEntity<Response> readAll() {
        StoreListDto stores = storeService.readAll();
        return ResponseEntity.ok(Response.success(stores));
    }

    @GetMapping("/stores/{id}")
    public ResponseEntity<Response> read(@PathVariable(name = "id") Long id) {
        StoreDto store = storeService.read(id);
        return ResponseEntity.ok(Response.success(store));
    }
}