package com.lucasserra.reactive_health_check.controller;

import com.lucasserra.reactive_health_check.model.HealthCheckProducerModel;
import com.lucasserra.reactive_health_check.model.SiterelicModel;
import com.lucasserra.reactive_health_check.service.HealthCheckService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    private final HealthCheckService service;

    public HealthCheckController(HealthCheckService service) {
        this.service = service;
    }

    @GetMapping("")
    public Flux<HealthCheckProducerModel> getAllHealth() {
        return service.checkAllSites();
    }

}
