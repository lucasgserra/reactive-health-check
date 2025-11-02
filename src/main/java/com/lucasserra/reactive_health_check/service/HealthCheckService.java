package com.lucasserra.reactive_health_check.service;

import com.lucasserra.reactive_health_check.client.HealthCheckClient;
import com.lucasserra.reactive_health_check.model.HealthCheckBrokenModel;
import com.lucasserra.reactive_health_check.model.HealthCheckResponseModel;
import com.lucasserra.reactive_health_check.model.HealthCheckUpModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class HealthCheckService {

    private final HealthCheckClient client;

    public HealthCheckService(HealthCheckClient client) {
        this.client = client;
    }

    public Flux<HealthCheckBrokenModel> getBrokenLinks() {
        return client.verifyBrokenLinks();
    }

    public Flux<HealthCheckUpModel> checkAllSites() {
        return client.getAllHealthUrls();
    }
}
