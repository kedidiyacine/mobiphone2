package com.services.DAOs.article;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

import com.models.TelephoneMobile;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.db.DBConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class testTelephoneMobileDAO {
    private static Connection testConnection;
    private static HikariDataSource dataSource;
    private static ArticleDAO<TelephoneMobile> articleDAO;
    private static TelephoneMobileDAO telephoneMobileDAO;

    @BeforeEach
    void setUp() {
        // connection to H2 in-memory database for testing with connection pool provided
        // by HikariCP
        try {

            HikariConfig hikariConfig = DBConfig.getConfig("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
            dataSource = new HikariDataSource(hikariConfig);
            testConnection = dataSource.getConnection();
            articleDAO = new ArticleDAO<TelephoneMobile>(testConnection);
            telephoneMobileDAO = new TelephoneMobileDAO(testConnection);

            executeSqlFile("test_telephone.sql");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void tearDown() {
        // Close the connection after all tests
        try {
            if (testConnection != null && !testConnection.isClosed()) {
                dataSource.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void executeSqlFile(String filePath) {
        try (InputStream inputStream = testTelephoneMobileDAO.class.getResourceAsStream(filePath)) {
            if (inputStream != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                        sb.append(line).append("\n");
                    }

                    testConnection.createStatement().execute(sb.toString());

                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.err.println("Error: Could not load SQL file: " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testEnregistrerEtRelationArticleTelephone() {
        // Test the enregistrer and trouver_par_cin methods
        TelephoneMobile telephoneMobile = new TelephoneMobile("samsung pro", 500.0,
                4, "1234", "5678", "samsung");
        telephoneMobile = telephoneMobileDAO.enregistrer(telephoneMobile);

        TelephoneMobile retrievedTelephoneMobile = telephoneMobileDAO.trouver_par_id(telephoneMobile.getId());

        assertNotNull(retrievedTelephoneMobile);

        assertEquals(telephoneMobile.getId(), retrievedTelephoneMobile.getId());
    }

}
