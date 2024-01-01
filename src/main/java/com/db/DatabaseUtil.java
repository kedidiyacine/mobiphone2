package com.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseUtil {
    private static final HikariDataSource dataSource;

    // Charger le pilote JDBC
    static {
        try {
            HikariConfig config = new DBConfig().getConfig();
            dataSource = new HikariDataSource(config);

        } catch (Exception e) {
            throw new RuntimeException("", e);
        }
    }

    // Obtenir une connexion à la base de données depuis le pool
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // Fermer la connexion à la base de données
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Optional: Close the connection pool when the application exits
    public static void closeConnectionPool() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
