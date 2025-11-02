package com.lucasserra.reactive_health_check.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiterelicModel {

    Long timestamp;
    String apiVersion;
    String apiStatus;
    Integer apiCode;
    String message;
    SiterelicMetaModel meta;
    SiterelicDataModel data;


}
