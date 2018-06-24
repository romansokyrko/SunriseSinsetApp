package com.sokyrko.sunrisesunsetapp.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Роман on 23.06.2018.
 */

public class LocationSunriseSunsetInfo {

    private String address;
    private Date sunsetTime;
    private Date sunriseTime;
    private Date firstLightTime;
    private Date lastLightTime;
    private int dayLength;
    private TimeZone timeZone;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getSunriseTime() {
        return sunriseTime;
    }

    public void setSunriseTime(Date sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    public Date getSunsetTime() {
        return sunsetTime;
    }

    public void setSunsetTime(Date sunsetTime) {
        this.sunsetTime = sunsetTime;
    }

    public int getDayLength() {
        return dayLength;
    }

    public Date getFirstLightTime() {
        return firstLightTime;
    }

    public void setFirstLightTime(Date firstLightTime) {
        this.firstLightTime = firstLightTime;
    }

    public Date getLastLightTime() {
        return lastLightTime;
    }

    public void setLastLightTime(Date lastLightTime) {
        this.lastLightTime = lastLightTime;
    }

    public void setDayLength(int dayLength) {
        this.dayLength = dayLength;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public Calendar getCalendarSunsetTime(){
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeZone(timeZone);
        calendar.setTime(sunsetTime);
        return calendar;
    }

    public Calendar getCalendarSunriseTime(){
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeZone(timeZone);
        calendar.setTime(sunriseTime);
        return calendar;
    }

    public Calendar getCalendarFirstLightTime(){
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeZone(timeZone);
        calendar.setTime(firstLightTime);
        return calendar;
    }

    public Calendar getCalendarLastLightTime(){
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeZone(timeZone);
        calendar.setTime(lastLightTime);
        return calendar;
    }

    public String getFormattedSunriseTime(){
        return formatTime(getCalendarSunriseTime());
    }

    public String getFormattedSunsetTime(){
        return formatTime(getCalendarSunsetTime());
    }

    public String getFormattedFirstLightTime(){
        return formatTime(getCalendarFirstLightTime());
    }

    public String getFormattedLastLightTime(){
        return formatTime(getCalendarLastLightTime());
    }

    private String formatTime(Calendar calendarTime) {
        StringBuilder result = new StringBuilder();
        result.append(calendarTime.get(Calendar.HOUR))
                .append(":")
                .append(formatMinutesSeconds(calendarTime.get(Calendar.MINUTE)))
                .append(":")
                .append(formatMinutesSeconds(calendarTime.get(Calendar.SECOND)))
                .append(" ");
        switch (calendarTime.get(Calendar.AM_PM)){
            case 0: result.append("AM");
                    break;
            case 1: result.append("PM");
                    break;
        }
        return result.toString();
    }

    private String formatMinutesSeconds(int i) {
        return i/10==0 ? "0"+i : String.valueOf(i);
    }

    public String getFormattedDayLength(){
        StringBuilder result = new StringBuilder();
        result.append(dayLength/60/60)
                .append(" hours, ")
                .append(formatMinutesSeconds((dayLength/60)%60))
                .append(" minutes");
        return result.toString();
    }

    public Calendar getCurrentCallendarTime(){
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeZone(timeZone);
        calendar.setTime(new Date(System.currentTimeMillis()));
        return calendar;
    }

    public String getFormattedCurentTime(){
        StringBuilder result = new StringBuilder();
        Calendar calendarTime = getCurrentCallendarTime();
        result.append(calendarTime.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US))
                .append(" ")
                .append(calendarTime.get(Calendar.DATE))
                .append(", ")
                .append(calendarTime.get(Calendar.YEAR))
                .append(". Current time: ")
                .append(formatTime(calendarTime))
                .append(" (")
                .append(timeZone.getID())
                .append(" timezone)");
        return result.toString();
    }

}
