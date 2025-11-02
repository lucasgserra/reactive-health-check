package com.lucasserra.reactive_health_check.client;

import com.lucasserra.reactive_health_check.config.HealthCheckUrlConfig;
import com.lucasserra.reactive_health_check.model.HealthCheckBrokenModel;
import com.lucasserra.reactive_health_check.model.HealthCheckUpModel;
import com.lucasserra.reactive_health_check.model.SiterelicBrokenModel;
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

import java.time.Instant;
import java.util.Date;

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

    public Mono<HealthCheckUpModel> getHealthUrls(String url) {
        return webClient.post()
                .uri("/up")
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-api-key", api_key)
                .bodyValue(new SiterelicBodyParser(url))
                .retrieve()
                .bodyToMono(SiterelicModel.class)
                .doOnNext(n -> {
                    System.out.println("iniciando coleta para a url " + n.getMeta().getUrl());
                })
                .map(siterelic -> {
                    HealthCheckUpModel producer = new HealthCheckUpModel();
                    producer.setResponse(siterelic.getData().getReasonPhrase());
                    producer.setStatusCode(siterelic.getData().getStatusCode());
                    producer.setDateTime(Date.from(Instant.now()).toString());;
                    producer.setUrl(url);
                    return producer;
                })
                .onErrorResume(e -> {
                    System.err.println("um erro ocorreu durante a coleta healthcheck da url " + url);
                    System.err.println("causa: " + e);
                    return Mono.empty();
                });
    }

    public Flux<HealthCheckUpModel> getAllHealthUrls() {
        return Flux.fromIterable(urls.getUrls())
                .flatMap(this::getHealthUrls);
    }

    public Flux<HealthCheckBrokenModel> verifyBrokenLinks() {
        return getAllHealthUrls()
                .filter(s -> s.getStatusCode()
                        != null && s.getStatusCode() >= 200 && s.getStatusCode() < 300)
                .flatMap(s -> webClient.post()
                        .uri("/brokenlink")
                        .header("x-api-key", api_key)
                        .bodyValue(new SiterelicBodyParser(s.getUrl()))
                        .retrieve()
                        .bodyToMono(SiterelicBrokenModel.class)
                        .map(siterelic -> {
                            HealthCheckBrokenModel producer = new HealthCheckBrokenModel();
                            producer.setBrokenLinks(siterelic.getData());
                            producer.setDateTime(Date.from(Instant.now()).toString());
                            return producer;
                        })
                        .onErrorResume(e -> {
                            System.err.println("um erro ocorreu durante a coleta de brokenlinks da url " + s.getUrl());
                            System.err.println("causa: " + e);
                            return Mono.empty();
                        }));
    }
}
