package com.lucasserra.reactive_health_check.model;

import lombok.Data;

import java.util.List;

@Data
public class SiterelicDataModel {

    Integer statusCode;
    String reasonPhrase;
}
