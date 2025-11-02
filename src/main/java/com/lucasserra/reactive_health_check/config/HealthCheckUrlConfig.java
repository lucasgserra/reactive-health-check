package com.lucasserra.reactive_health_check.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Component
public class HealthCheckUrlConfig {

    @Value("#{'${apps.url}'.split(',')}")
    private List<String> urls;

}
