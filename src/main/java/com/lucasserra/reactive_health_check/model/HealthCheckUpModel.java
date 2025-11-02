package com.lucasserra.reactive_health_check.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HealthCheckUpModel {

    String dateTime;
    String url;
    Integer statusCode;
    String response;

}
