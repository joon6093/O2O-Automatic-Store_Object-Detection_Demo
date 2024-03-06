package com.IIA.o2o_automatic_store_springmvc_java.service.snack;


import com.IIA.o2o_automatic_store_springmvc_java.dto.snack.SnackDto;
import com.IIA.o2o_automatic_store_springmvc_java.entity.snack.Position;
import com.IIA.o2o_automatic_store_springmvc_java.entity.snack.Snack;
import com.IIA.o2o_automatic_store_springmvc_java.exception.SnackNotFoundException;
import com.IIA.o2o_automatic_store_springmvc_java.repository.snack.SnackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class SnackService {

    private final SnackRepository snackRepository;
    private final SnackAnalysisClient snackAnalysisClient;

    public List<Snack> findAllSnacks() {
        return snackRepository.findAll();
    }

    public Snack findSnack(Long id) {
        return snackRepository.findById(id).orElseThrow(
                () -> new SnackNotFoundException(id.toString()));
    }

    public List<Snack> analyzeSnacks(List<MultipartFile> images) {
        List<SnackDto> snackDtos = snackAnalysisClient.analyzeSnacks(images);

        List<Snack> snacks = snackDtos.stream()
                .map(dto -> Snack.builder()
                        .filename(dto.getFilename())
                        .objectName(dto.getObject_name())
                        .position(new Position(dto.getPosition().getX1(), dto.getPosition().getX2(), dto.getPosition().getY1(), dto.getPosition().getY2()))
                        .build())
                .collect(Collectors.toList());

        snackRepository.saveAll(snacks);
        return snacks;
    }
}


