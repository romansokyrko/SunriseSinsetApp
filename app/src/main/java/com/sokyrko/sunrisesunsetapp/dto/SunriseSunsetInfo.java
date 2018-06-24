package com.sokyrko.sunrisesunsetapp.dto;

/**
 * Created by Роман on 21.06.2018.
 */

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class SunriseSunsetInfo {
    @JsonProperty("sunrise")
    private Date sunrise;
    @JsonProperty("sunset")
    private Date sunset;
    @JsonProperty("solar_noon")
    private Date solarNoon;
    @JsonProperty("day_length")
    private int dayLength;
    @JsonProperty("civil_twilight_begin")
    private Date civilTwilightBegin;
    @JsonProperty("civil_twilight_end")
    private Date civilTwilightEnd;
    @JsonProperty("nautical_twilight_begin")
    private Date nauticalTwilightBegin;
    @JsonProperty("nautical_twilight_end")
    private Date nauticalTwilightEnd;
    @JsonProperty("astronomical_twilight_begin")
    private Date astronomicalTwilightBegin;
    @JsonProperty("astronomical_twilight_end")
    private Date astronomicalTwilightEnd;

    public Date getSunrise() {
        return sunrise;
    }

    public Date getSunset() {
        return sunset;
    }

    public Date getSolarNoon() {
        return solarNoon;
    }

    public int getDayLength() {
        return dayLength;
    }

    public Date getCivilTwilightBegin() {
        return civilTwilightBegin;
    }

    public Date getCivilTwilightEnd() {
        return civilTwilightEnd;
    }

    public Date getNauticalTwilightBegin() {
        return nauticalTwilightBegin;
    }

    public Date getNauticalTwilightEnd() {
        return nauticalTwilightEnd;
    }

    public Date getAstronomicalTwilightBegin() {
        return astronomicalTwilightBegin;
    }

    public Date getAstronomicalTwilightEnd() {
        return astronomicalTwilightEnd;
    }
}
