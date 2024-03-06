package com.IIA.o2o_automatic_store_springmvc_java.service.snack;

import com.IIA.o2o_automatic_store_springmvc_java.dto.snack.SnackDto;
import com.IIA.o2o_automatic_store_springmvc_java.exception.ImageReadException;
import com.IIA.o2o_automatic_store_springmvc_java.exception.NullResponseFromApiException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;

@Service
public class SnackAnalysisClientImpl implements SnackAnalysisClient {

    private final WebClient webClient;

    public SnackAnalysisClientImpl(@Value("${spring.flask.base-url}") String flaskBaseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(flaskBaseUrl)
                .build();
    }

    @Override
    public List<SnackDto> analyzeSnacks(List<MultipartFile> images) {
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

        return webClient.post()
                .uri("/detect")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<SnackDto>>() {})
                .onErrorResume(WebClientRequestException.class, e -> Mono.error(new NullResponseFromApiException()))
                .block();
    }
}

