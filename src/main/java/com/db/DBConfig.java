package com.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;

public class DBConfig {

    private static final String PROPERTIES_FILE = "config.properties";

    private Properties properties;

    public DBConfig() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + PROPERTIES_FILE);
                return;
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HikariConfig getConfig() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(getJdbcUrl());
        config.setUsername(getDbUser());
        config.setPassword(getDbPassword());

        // Set other HikariCP options as needed

        return config;
    }

    // for the TEST DB
    public static HikariConfig getConfig(String jdbcUrl) {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(jdbcUrl);

        // Set other HikariCP options as needed
        config.setUsername("sa"); // default H2 username
        config.setPassword(""); // default H2 password
        config.setDriverClassName("org.h2.Driver"); // H2 driver class
        config.setMaximumPoolSize(10); // Maximum number of connections in the pool
        // ... (other configuration options)

        return config;
    }

    private String getJdbcUrl() {
        return properties.getProperty("jdbc_url");
    }

    private String getDbUser() {
        return properties.getProperty("db_user");
    }

    private String getDbPassword() {
        return properties.getProperty("db_password");
    }

}
