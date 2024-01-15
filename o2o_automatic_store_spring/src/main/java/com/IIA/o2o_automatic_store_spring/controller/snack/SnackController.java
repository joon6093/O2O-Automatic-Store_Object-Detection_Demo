package com.IIA.o2o_automatic_store_spring.controller.snack;

import com.IIA.o2o_automatic_store_spring.dto.response.Response;
import com.IIA.o2o_automatic_store_spring.service.snack.SnackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/snacks")
public class SnackController {

    private final SnackService snackService;

    @GetMapping("/list")
    public ResponseEntity<Response> retrieveAllSnacks() {
        return ResponseEntity.status(HttpStatus.OK).body(Response.success(snackService.findAllSnacks()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> retrieveSnackById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(Response.success(snackService.findSnack(id)));
    }

    @PostMapping("/analyze")
    public ResponseEntity<Response> analyzeSnacks(@RequestParam("images") MultipartFile[] images) {
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.success(snackService.analyzeSnacks(Arrays.asList(images))));

    }
}
