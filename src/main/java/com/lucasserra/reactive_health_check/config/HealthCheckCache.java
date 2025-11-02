package com.lucasserra.reactive_health_check.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.lucasserra.reactive_health_check.service.HealthCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

@Component
public class HealthCheckCache {

    private final HealthCheckService service;
    private final ConcurrentMap<Object, Object> cacheUp;
    private final ConcurrentMap<Object, Object> cacheBroken;

    @Value("${siterelic.ttl.cache}")
    private Integer cacheTTL;

    public HealthCheckCache(HealthCheckService service) {
        this.service = service;

        int ttl = cacheTTL != null ? cacheTTL : 1;

        this.cacheUp = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(ttl))
                .build()
                .asMap();

        this.cacheBroken = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(ttl))
                .build()
                .asMap();
    }

    @PostConstruct
    private void initCache() {
        service.checkAllSites()
                .onErrorContinue((e, o) -> System.err.println("erro checkAllSites inicial: " + e))
                .collectList()
                .subscribe(list -> cacheUp.put("allSites", list));

        service.getBrokenLinks()
                .onErrorContinue((e, o) -> System.err.println("erro getBrokenLinks inicial: " + e))
                .collectList()
                .subscribe(list -> cacheBroken.put("brokenLinks", list));
        //startPeriodicCollection();
    }

    @Scheduled(fixedRate = 60000)
    void scheduled() {
        initCache();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getCachedUp() {
        return (List<T>) cacheUp.getOrDefault("allSites", List.of());
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getCachedBroken() {
        return (List<T>) cacheBroken.getOrDefault("brokenLinks", List.of());
    }
}
