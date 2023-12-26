package com.utils;

import java.util.Locale;
import java.util.Map;

public class StringUtils {

    private StringUtils() {
    }

    public static String capitalizeWord(String word) {
        return word.substring(0, 1).toUpperCase(Locale.ENGLISH) +
                word.substring(1);
    }

    public static String buildSqlUpdateStatementFromMap(Map<String, Object> updates) {
        StringBuilder sqlBuilder = new StringBuilder("UPDATE client SET ");
        updates.forEach((key, value) -> sqlBuilder.append(key).append("=?, "));
        sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length()); // Remove the last comma
        sqlBuilder.append(" WHERE id=?");

        return sqlBuilder.toString();
    }

    public static String buildChangeMessage(Long clientId, Map<String, Map<String, String>> columnModifications) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(String.format("Changed row with id: %d%n", clientId));

        for (Map.Entry<String, Map<String, String>> columnEntry : columnModifications.entrySet()) {
            String columnName = columnEntry.getKey();
            String oldCin = columnEntry.getValue().get("old");
            String newCin = columnEntry.getValue().get("new");

            messageBuilder.append(String.format("\t- %s changed from '%s' to '%s'%n", columnName, oldCin, newCin));
        }

        return messageBuilder.toString();
    }

}
