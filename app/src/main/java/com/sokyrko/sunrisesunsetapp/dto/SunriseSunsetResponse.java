package com.sokyrko.sunrisesunsetapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Роман on 22.06.2018.
 */

public class SunriseSunsetResponse {
    @JsonProperty("results")
    private SunriseSunsetInfo sunriseSunsetInfo;
    @JsonProperty("status")
    private String status;

    public SunriseSunsetInfo getSunriseSunsetInfo() {
        return sunriseSunsetInfo;
    }

    public String getStatus() {
        return status;
    }
}
