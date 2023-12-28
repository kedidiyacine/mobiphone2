package com.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Constants {

    private Constants() {
    }

    public enum KEY {
        LOGIN, PASSWORD, ROLE
    }

    // Add any other constants here

    // Naming convention for constants: UPPERCASE_WITH_UNDERSCORES
    public static final String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String CONFIRMATION_MESSAGE = "Are you sure you want to confirm these changes?";

    public static final Set<String> NON_EDITABLE_COLUMNS = new HashSet<>(
            Arrays.asList("id", "date_creation", "date_maj"));

}
