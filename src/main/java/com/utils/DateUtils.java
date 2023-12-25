package com.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private DateUtils() {
        // private constructor to prevent instantiation
    }

    public static String getFormattedDate(LocalDateTime date) {
        if (date == null) {
            // Decide how to handle null values
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT_PATTERN);
        return date.format(formatter);
    }

}
