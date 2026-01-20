package com.raining.simple_planner.global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeUtil {
    private static final String TIME_FORMAT = "HH:mm";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm";

    /**
     * LocalTime 을 "HH:mm" 형식의 문자열로 변환
     * @param time
     * @return
     */
    public static String localTimeToString(LocalTime time) {
        return time.format(java.time.format.DateTimeFormatter.ofPattern(TIME_FORMAT));
    }

    /**
     * "HH:mm" 형식의 문자열을 LocalTime 으로 변환
     * @param timeString
     * @return
     */
    public static LocalTime stringToLocalTime(String timeString) {
        return LocalTime.parse(timeString, java.time.format.DateTimeFormatter.ofPattern(TIME_FORMAT));
    }

    /**
     * LocalDate 를 "yyyy-MM-dd" 형식의 문자열로 변환
     * @param date
     * @return
     */
    public static String localDateToString(LocalDate date) {
        return date.format(java.time.format.DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    /**
     * "yyyy-MM-dd" 형식의 문자열을 LocalDate 로 변환
     * @param dateString
     * @return
     */
    public static LocalDate stringToLocalDate(String dateString) {
        return java.time.LocalDate.parse(dateString, java.time.format.DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    /**
     * LocalDateTime 을 "yyyy-MM-dd'T'HH:mm" 형식의 문자열로 변환
     * @param dateTime
     * @return
     */
    public static String localDateTimeToString(LocalDateTime dateTime) {
        return dateTime.format(java.time.format.DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
    }

    /**
     * "yyyy-MM-dd'T'HH:mm" 형식의 문자열을 LocalDateTime 으로 변환
     * @param dateTimeString
     * @return
     */
    public static LocalDateTime stringToLocalDateTime(String dateTimeString) {
        return java.time.LocalDateTime.parse(dateTimeString, java.time.format.DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
    }

    public static LocalDateTime combineDateAndTime(LocalDate date, LocalTime time) {
        return LocalDateTime.of(date, time);
    }

    public static LocalDateTime combineDateAndTime(String dateString, String timeString) {
        LocalDate date = stringToLocalDate(dateString);
        LocalTime time = stringToLocalTime(timeString);
        return LocalDateTime.of(date, time);
    }

}
