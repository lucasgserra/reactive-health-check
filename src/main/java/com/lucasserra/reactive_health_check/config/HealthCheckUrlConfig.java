package com.lucasserra.reactive_health_check.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HealthCheckUrlConfig {

    @Value("#{'apps.url'.split(',')}")
    private List<String> urls;

    public List<String> getUrls() {
        return urls;
    }
}
