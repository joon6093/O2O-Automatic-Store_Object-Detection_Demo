package com.IIA.o2o_automatic_store_springmvc_java.controller.healthcheck;

import com.IIA.o2o_automatic_store_springmvc_java.service.healthcheck.HealthCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/health")
public class HealthCheckController {

    private final HealthCheckService healthCheckService;

    @GetMapping("/check-flask")
    public Mono<String> checkFlask() {
        return healthCheckService.checkFlaskHealth();
    }
}
