package com.utils;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StringUtils {

    private StringUtils() {
    }

    public static String capitalizeWord(String word) {
        return word.substring(0, 1).toUpperCase(Locale.ENGLISH) +
                word.substring(1);
    }

    public static String buildSqlUpdateStatementFromMap(Map<String, Object> updates, String sqlTableName,
            String where) {

        StringBuilder sqlBuilder = new StringBuilder("UPDATE " + sqlTableName + " SET ");

        updates.forEach((key, value) -> sqlBuilder.append(key).append("=?, "));
        sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length()); // Remove the last comma
        sqlBuilder.append(" WHERE ").append(where).append("=?");

        return sqlBuilder.toString();
    }

    public static String buildSQLDeleteStatement(String sqlTableName, String primaryKey) {

        return String.format("DELETE FROM %s WHERE %s =?", sqlTableName, primaryKey);

    }

    public static String buildSQLSelectJoinStatement(String sqlTableName1, String sqlTableName2, String primaryKey1,
            String primaryKey2, String where) {

        return String.format("SELECT * FROM %s INNER JOIN %s ON %s.%s = %s.%s WHERE %s = ?", sqlTableName1,
                sqlTableName2, sqlTableName1, primaryKey1, sqlTableName2, primaryKey2, where);
    }

    public static String buildSQLSelectCountStatement(String sqlTableName) {

        return String.format("SELECT COUNT(*) FROM %s", sqlTableName);
    }

    public static String buildChangeMessage(Serializable id, Map<String, Map<String, ?>> columnModifications) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(String.format("Changed row with id: %d%n", id));

        for (Map.Entry<String, Map<String, ?>> columnEntry : columnModifications.entrySet()) {
            String columnName = columnEntry.getKey();
            String oldCin = columnEntry.getValue().get("old").toString();
            String newCin = columnEntry.getValue().get("new").toString();

            messageBuilder.append(String.format("\t- %s changed from '%s' to '%s'%n", columnName, oldCin, newCin));
        }

        return messageBuilder.toString();
    }

    public static String buildDeletionMessage(int itemCount) {
        String headerText = Constants.DELETION_MESSAGE_HEADER;

        return String.format(headerText, itemCount);

    }

    public static String buildInsertStatement(String sqlTableName, List<String> columns) {
        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO %s (");

        // Use String.join to concatenate column names with commas
        String columnNames = String.join(", ", columns);

        sqlBuilder.append(columnNames)
                .append(") VALUES(");

        // Add placeholders for values
        for (int i = 0; i < columns.size(); i++) {
            sqlBuilder.append("?");
            if (i < columns.size() - 1) {
                sqlBuilder.append(", ");
            }
        }

        sqlBuilder.append(")");

        return String.format(sqlBuilder.toString(), sqlTableName);
    }

}
