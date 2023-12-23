package com.services.DAOs.client;

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
import com.models.Client;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

class TestImplClientDAO {

    private static Connection testConnection;
    private static HikariDataSource dataSource;
    private static ClientDAO clientDAO;

    @BeforeEach
    void setUp() {
        // connection to H2 in-memory database for testing with connection pool provided
        // by HikariCP
        try {

            HikariConfig hikariConfig = DBConfig.getConfig("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
            dataSource = new HikariDataSource(hikariConfig);
            testConnection = dataSource.getConnection();
            clientDAO = new ClientDAO(testConnection);

            executeSqlFile("test_client.sql");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void executeSqlFile(String filePath) {
        try (InputStream inputStream = TestImplClientDAO.class.getResourceAsStream(filePath)) {
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
    void testTrouverParPage() {
        int page = 3;
        int total_pages = 11;
        // Insert a test client
        for (int i = 1; i < total_pages; i++) {
            for (int j = 1; j < total_pages; j++) {
                Client testClient = new Client((j * i) + "", "Jane", "Doe",
                        "456 Oak St", "jane.doe@example.com");
                clientDAO.enregistrer(testClient);
            }

        }

        // Retrieve clients by page
        List<Client> clients = clientDAO.trouver_par_page(page, 10);
        assertNotNull(clients);

        for (int i = 0; i < total_pages - 1; i++) {
            System.out.println("expected: " + (((i + 1) * page) + ""));
            System.out.println("got: " + clients.get(i).getCin());

            assertEquals(((i + 1) * page) + "", clients.get(i).getCin());
        }
        assertEquals(10, clients.size());
    }

    @Test
    void testEnregistrerEtTrouverParCin() {
        // Test the enregistrer and trouver_par_cin methods
        Client client = new Client("123456", "John", "Doe",
                "123 Main St", "john.doe@example.com");
        clientDAO.enregistrer(client);

        Client retrievedClient = clientDAO.trouver_par_cin("123456");
        assertNotNull(retrievedClient);
        assertEquals("123456", retrievedClient.getCin());
    }

    @Test
    void testTrouverTout() {
        // Test the trouver_tout method
        int expected = 0;
        List<Client> clients = clientDAO.trouver_tout();
        expected = clients.size();

        // Insert a test client
        Client testClient = new Client("789012", "Jane", "Doe",
                "456 Oak St", "jane.doe@example.com");
        clientDAO.enregistrer(testClient);

        // Retrieve all clients
        clients = clientDAO.trouver_tout();
        assertNotNull(clients);
        assertEquals(expected + 1, clients.size());
    }

    @Test
    void testSupprimerParId() {
        // Test the supprimer_par_id method
        Client testClient = new Client("7890123", "Jane", "Doe",
                "456 Oak St", "jane.doe@example.com");
        clientDAO.enregistrer(testClient);

        Client retrievedClient = clientDAO.trouver_par_cin("7890123");
        assertNotNull(retrievedClient);

        clientDAO.supprimer_par_id(retrievedClient.getId());

        retrievedClient = clientDAO.trouver_par_cin("7890123");
        assertNull(retrievedClient);
    }

}
