package com.lucasserra.reactive_health_check.service;

import com.lucasserra.reactive_health_check.client.HealthCheckClient;
import com.lucasserra.reactive_health_check.model.HealthCheckProducerModel;
import com.lucasserra.reactive_health_check.model.SiterelicModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class HealthCheckService {

    private final HealthCheckClient client;

    public HealthCheckService(HealthCheckClient client) {
        this.client = client;
    }

    public Flux<HealthCheckProducerModel> checkAllSites() {
        return client.getAllHealthUrls();
    }

}
