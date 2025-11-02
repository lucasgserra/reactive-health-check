package com.lucasserra.reactive_health_check.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.lucasserra.reactive_health_check.service.HealthCheckService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

@Component
public class HealthCheckCache {

    private final HealthCheckService service;
    private final ConcurrentMap<Object, Object> cacheUp;
    private final ConcurrentMap<Object, Object> cacheBroken;

    @Value("${siterelic.update.cache}")
    private Integer cacheDelay;

    @Value("${siterelic.ttl.cache}")
    private Integer cacheTTL;

    public HealthCheckCache(HealthCheckService service) {
        this.service = service;

        int ttl = cacheTTL!=null?cacheTTL:1;

        this.cacheUp = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(ttl))
                .build()
                .asMap();

        this.cacheBroken = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(ttl))
                .build()
                .asMap();
    }
    private void startPeriodicCollection() {

        int delay = cacheDelay!=null?cacheDelay:1;

        Flux.interval(Duration.ZERO, Duration.ofMinutes(delay))
                .flatMap(tick -> service.checkAllSites())
                .collectList()
                .doOnNext(list -> cacheUp.put("allSites", list))
                .subscribe();

        Flux.interval(Duration.ZERO, Duration.ofMinutes(delay))
                .flatMap(tick -> service.getBrokenLinks())
                .collectList()
                .doOnNext(list -> cacheBroken.put("brokenLinks", list))
                .subscribe();
    }

    @PostConstruct
    private void initCache() {
        service.checkAllSites()
                .collectList()
                .doOnNext(list -> cacheUp.put("allSites", list))
                .block();
        service.getBrokenLinks()
                .collectList()
                .doOnNext(list -> cacheBroken.put("brokenLinks", list))
                .block();
        startPeriodicCollection();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getCachedUp() {
        System.out.println(cacheUp.get("allSites"));
        return (List<T>) cacheUp.getOrDefault("allSites", List.of());
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getCachedBroken() {
        return (List<T>) cacheBroken.getOrDefault("brokenLinks", List.of());
    }
}
