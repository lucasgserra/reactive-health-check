package com.lucasserra.reactive_health_check.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiterelicBrokenModel {

    Long timestamp;
    String apiVersion;
    String apiStatus;
    Integer apiCode;
    String message;
    SiterelicMetaModel meta;
    List<SiterelicDataBrokenModel> data;


}
