package com.IIA.o2o_automatic_store_spring.service.healthcheck;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class HealthCheckService {

    private final WebClient webClient;

    public HealthCheckService(@Value("${spring.flask.base-url}") String flaskBaseUrl) {
        this.webClient = WebClient.builder().baseUrl(flaskBaseUrl).build();
    }

    public Mono<String> checkFlaskHealth() {
        return webClient.get()
                .uri("/health-check")
                .retrieve()
                .bodyToMono(String.class);
    }
}
