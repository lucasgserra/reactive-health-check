package com.lucasserra.reactive_health_check.model.consumer;

import lombok.Data;

@Data
public class SiterelicDataConsumer {

    Integer statusCode;
    String reasonPhrase;

}
