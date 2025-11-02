package com.lucasserra.reactive_health_check.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucasserra.reactive_health_check.config.HealthCheckUrlConfig;
import com.lucasserra.reactive_health_check.model.HealthCheckProducerModel;
import com.lucasserra.reactive_health_check.model.SiterelicModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class HealthCheckClient {

    @Data
    @Getter
    @Setter
    public static class SiterelicBodyParser {
        String url;
        public SiterelicBodyParser(String url) {
            this.url = url;
        }
    }
    @Value("${siterelic.apikey}")
    private String api_key;
    private final WebClient webClient;
    private final HealthCheckUrlConfig urls;

    public HealthCheckClient(WebClient.Builder builder, HealthCheckUrlConfig urls) {
        this.webClient = builder.baseUrl("https://api.siterelic.com").build();
        this.urls = urls;
    }

    public Mono<HealthCheckProducerModel> getHealthUrls(String url) {
        return webClient.post()
                .uri("/up")
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-api-key", api_key)
                .bodyValue(new SiterelicBodyParser(url))
                .retrieve()
                .bodyToMono(SiterelicModel.class)
                .map(siterelic -> {
                    HealthCheckProducerModel producer = new HealthCheckProducerModel();
                    producer.setResponse(siterelic.getData().getReasonPhrase());
                    producer.setStatusCode(siterelic.getData().getStatusCode());
                    producer.setUrl(url);
                    return producer;
                })
                .onErrorResume(e -> {
                    System.err.println("um erro ocorreu durante a coleta healthcheck da url " + url);
                    System.err.println("causa: " + e + api_key);
                    return Mono.empty();
                });
    }

    public Flux<HealthCheckProducerModel> getAllHealthUrls() {
        return Flux.fromIterable(urls.getUrls())
                .flatMap(this::getHealthUrls);
    }
}
