package com.IIA.o2o_automatic_store_spring.service.snack;


import com.IIA.o2o_automatic_store_spring.dto.snack.SnackDto;
import com.IIA.o2o_automatic_store_spring.entity.snack.Position;
import com.IIA.o2o_automatic_store_spring.entity.snack.Snack;
import com.IIA.o2o_automatic_store_spring.exception.ImageReadException;
import com.IIA.o2o_automatic_store_spring.exception.NullResponseFromApiException;
import com.IIA.o2o_automatic_store_spring.exception.SnackNotFoundException;
import com.IIA.o2o_automatic_store_spring.repository.snack.SnackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SnackService {

    private final SnackRepository snackRepository;

    @Value("${spring.flask.base-url}")
    private String flaskBaseUrl;

    public List<Snack> findAllSnacks() {
        return snackRepository.findAll();
    }

    public Snack findSnack(Long id) {
        return snackRepository.findById(id).orElseThrow(
                () -> new SnackNotFoundException(id.toString()));
    }

    @Transactional
    public List<Snack> analyzeSnacks(List<MultipartFile> images) {
        String baseUrl = flaskBaseUrl + "/detect";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        images.forEach(image -> {
            try {
                Resource resource = new ByteArrayResource(image.getBytes()) {
                    @Override
                    public String getFilename() {
                        return image.getOriginalFilename();
                    }
                };
                body.add("images", resource);
            } catch (IOException e) {
                throw new ImageReadException(e);
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<List<SnackDto>> responseEntity;
        try {
            responseEntity= restTemplate.exchange(baseUrl, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<List<SnackDto>>() {});
        } catch (ResourceAccessException e) {
            throw new NullResponseFromApiException();
        }
        List<SnackDto> snackDtos = responseEntity.getBody();
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
