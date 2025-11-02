package com.lucasserra.reactive_health_check.controller;

import com.lucasserra.reactive_health_check.config.HealthCheckCache;
import com.lucasserra.reactive_health_check.model.HealthCheckBrokenModel;
import com.lucasserra.reactive_health_check.model.HealthCheckUpModel;
import com.lucasserra.reactive_health_check.service.HealthCheckService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    private final HealthCheckCache cache;

    public HealthCheckController(HealthCheckCache cache) {
        this.cache = cache;
    }

    @GetMapping("")
    public Flux<HealthCheckUpModel> getAllHealth() {
        return Flux.fromIterable(cache.getCachedUp());
    }
    @GetMapping("links")
    public Flux<HealthCheckBrokenModel> getBrokenLink() {
        return Flux.fromIterable(cache.getCachedBroken());
    }
}
