package com.IIA.o2o_automatic_store_springmvc_java.service.snack;


import com.IIA.o2o_automatic_store_springmvc_java.dto.snack.SnackDto;
import com.IIA.o2o_automatic_store_springmvc_java.entity.snack.Position;
import com.IIA.o2o_automatic_store_springmvc_java.entity.snack.Snack;
import com.IIA.o2o_automatic_store_springmvc_java.exception.ImageReadException;
import com.IIA.o2o_automatic_store_springmvc_java.exception.NullResponseFromApiException;
import com.IIA.o2o_automatic_store_springmvc_java.exception.SnackNotFoundException;
import com.IIA.o2o_automatic_store_springmvc_java.repository.snack.SnackRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SnackService {

    private final SnackRepository snackRepository;
    private final WebClient webClient;

    public SnackService(SnackRepository snackRepository, @Value("${spring.flask.base-url}") String flaskBaseUrl) {
        this.snackRepository = snackRepository;
        this.webClient = WebClient.builder()
                .baseUrl(flaskBaseUrl)
                .build();
    }
    public List<Snack> findAllSnacks() {
        return snackRepository.findAll();
    }

    public Snack findSnack(Long id) {
        return snackRepository.findById(id).orElseThrow(
                () -> new SnackNotFoundException(id.toString()));
    }

    public List<Snack> analyzeSnacks(List<MultipartFile> images) {
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

        List<SnackDto> snackDtos = webClient.post()
                .uri("/detect")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<SnackDto>>() {})
                .onErrorResume(WebClientRequestException.class, e -> Mono.error(new NullResponseFromApiException()))
                .block();

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

