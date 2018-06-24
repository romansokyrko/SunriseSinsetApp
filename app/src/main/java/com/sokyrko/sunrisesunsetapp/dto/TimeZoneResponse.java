package com.sokyrko.sunrisesunsetapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Роман on 23.06.2018.
 */

public class TimeZoneResponse {
    @JsonProperty("dstOffset")
    private int dstOffset;
    @JsonProperty("rawOffset")
    private int rawOffset;
    @JsonProperty("timeZoneId")
    private String timeZoneId;
    @JsonProperty("timeZoneName")
    private String timeZoneName;
    @JsonProperty("status")
    private String status;
    @JsonProperty("errorMessage")
    private String errorMessage;

    public int getDstOffset() {
        return dstOffset;
    }

    public int getRawOffset() {
        return rawOffset;
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public String getTimeZoneName() {
        return timeZoneName;
    }

    public String getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
