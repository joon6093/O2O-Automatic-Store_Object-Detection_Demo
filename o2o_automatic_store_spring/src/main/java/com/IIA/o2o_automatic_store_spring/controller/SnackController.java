package com.IIA.o2o_automatic_store_spring.controller;

import com.IIA.o2o_automatic_store_spring.dto.MessageDto;
import com.IIA.o2o_automatic_store_spring.entity.Snack;
import com.IIA.o2o_automatic_store_spring.enums.StatusEnum;
import com.IIA.o2o_automatic_store_spring.service.SnackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SnackController {

    private final SnackService snackService;

    @GetMapping("/snacks")
    public ResponseEntity<MessageDto> retrieveAllSnacks() {
        List<Snack> snacks = snackService.findAllSnacks();
        return ResponseEntity.ok(new MessageDto(StatusEnum.OK, "Success", snacks));
    }

    @GetMapping("/snacks/{id}")
    public ResponseEntity<MessageDto> retrieveSnackById(@PathVariable Long id) {
        return ResponseEntity.ok(new MessageDto(StatusEnum.OK, "Success", snackService.findById(id)));
    }

    @PostMapping("/snacks/analyze")
    public ResponseEntity<MessageDto> analyzeSnacks(@RequestParam("images") MultipartFile[] images) {
        List<Snack> snacks = snackService.analyzeSnacks(Arrays.asList(images));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageDto(StatusEnum.OK, "Snack analysis completed successfully", snacks));
    }
}
