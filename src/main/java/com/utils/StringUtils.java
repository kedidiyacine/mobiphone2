package com.utils;

import java.util.Locale;
import java.util.Map;
import java.io.Serializable;

public class StringUtils {

    private StringUtils() {
    }

    public static String capitalizeWord(String word) {
        return word.substring(0, 1).toUpperCase(Locale.ENGLISH) +
                word.substring(1);
    }

    public static String buildSqlUpdateStatementFromMap(Map<String, Object> updates, String sqlTableName) {
        StringBuilder sqlBuilder = new StringBuilder("UPDATE " + sqlTableName + " SET ");
        updates.forEach((key, value) -> sqlBuilder.append(key).append("=?, "));
        sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length()); // Remove the last comma
        sqlBuilder.append(" WHERE id=?");

        return sqlBuilder.toString();
    }

    public static String buildChangeMessage(Serializable id, Map<String, Map<String, String>> columnModifications) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(String.format("Changed row with id: %d%n", id));

        for (Map.Entry<String, Map<String, String>> columnEntry : columnModifications.entrySet()) {
            String columnName = columnEntry.getKey();
            String oldCin = columnEntry.getValue().get("old");
            String newCin = columnEntry.getValue().get("new");

            messageBuilder.append(String.format("\t- %s changed from '%s' to '%s'%n", columnName, oldCin, newCin));
        }

        return messageBuilder.toString();
    }

}
