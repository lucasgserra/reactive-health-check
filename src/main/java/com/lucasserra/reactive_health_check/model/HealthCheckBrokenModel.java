package com.lucasserra.reactive_health_check.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HealthCheckBrokenModel {

    String dateTime;
    List<SiterelicDataBrokenModel> brokenLinks;

}
