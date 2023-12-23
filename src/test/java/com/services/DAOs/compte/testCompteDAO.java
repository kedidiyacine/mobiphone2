package com.services.DAOs.compte;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.db.DBConfig;
import com.models.Compte;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

class testCompteDAO {
    private static Connection testConnection;
    private static CompteDAO compteDAO;
    private static HikariDataSource dataSource;

    @BeforeEach
    void setUp() {
        try {
            HikariConfig hikariConfig = DBConfig.getConfig("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
            dataSource = new HikariDataSource(hikariConfig);
            testConnection = dataSource.getConnection();
            compteDAO = new CompteDAO(testConnection);

            executeSqlFile("test_compte.sql");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void executeSqlFile(String filePath) {
        try (InputStream inputStream = testCompteDAO.class.getResourceAsStream(filePath)) {
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

    @Test
    void testEnregistrerEtTrouverParLogin() {
        // Test the enregistrer and trouver_par_login methods
        Compte compte = new Compte("testUser", "testPassword", "agent commercial");
        compteDAO.enregistrer(compte);

        Compte retrievedCompte = compteDAO.trouver_par_login("testUser");
        assertNotNull(retrievedCompte);
        assertEquals("testUser", retrievedCompte.getLogin());
    }

    @Test
    void testTrouverTout() {
        // Test the trouver_tout method
        int expected = 0;
        List<Compte> comptes = compteDAO.trouver_tout();
        expected = comptes.size();

        // Insert a test user
        Compte testUser = new Compte("testUser1", "testPassword", "agent commercial");
        compteDAO.enregistrer(testUser);

        // Retrieve all users
        comptes = compteDAO.trouver_tout();
        assertNotNull(comptes);
        assertEquals(expected + 1, comptes.size());
    }

    @Test
    void testSupprimerParId() {
        // Test the supprimer_par_id method
        Compte testUser = new Compte("testUser2", "testPassword", "agent commercial");
        compteDAO.enregistrer(testUser);

        Compte retrievedCompte = compteDAO.trouver_par_login("testUser2");
        assertNotNull(retrievedCompte);

        compteDAO.supprimer_par_id(retrievedCompte.getId());

        retrievedCompte = compteDAO.trouver_par_login("testUser2");
        assertNull(retrievedCompte);
    }
}
