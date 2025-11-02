package com.lucasserra.reactive_health_check;

import com.lucasserra.reactive_health_check.config.HealthCheckUrlConfig;
import com.lucasserra.reactive_health_check.model.SiterelicModel;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class HealthCheckClient {

    public static class SiterelicBodyParser {
        String url;
        public SiterelicBodyParser(String url) {
            this.url = url;
        }
    }
    private final WebClient webClient;
    private final HealthCheckUrlConfig urls;

    public HealthCheckClient(WebClient.Builder builder, HealthCheckUrlConfig urls) {
        this.webClient = builder.baseUrl("https://siterelic.com").build();
        this.urls = urls;
    }

    public Mono<SiterelicModel> getHealthUrls(String url) {
        return webClient.post()
                .uri("/up")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new SiterelicBodyParser(url))
                .retrieve()
                .bodyToMono(SiterelicModel.class)
                .onErrorResume(e -> {
                    System.err.println("um erro ocorreu durante a coleta healthcheck da url " + url);
                    return Mono.empty();
                });

    }

    public Flux<SiterelicModel> getAllHealthUrls() {
        return Flux.fromIterable(urls.getUrls())
                .flatMap(this::getHealthUrls);
    }
}
