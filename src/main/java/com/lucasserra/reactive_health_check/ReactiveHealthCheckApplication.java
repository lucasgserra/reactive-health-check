package com.lucasserra.reactive_health_check;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReactiveHealthCheckApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveHealthCheckApplication.class, args);
	}

}
