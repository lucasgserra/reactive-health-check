package com.lucasserra.reactive_health_check.model.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiterelicConsumer {

    Long timestamp;
    String apiVersion;
    String apiStatus;
    Integer apiCode;
    String message;
    SiterelicMetaConsumer meta;


}
