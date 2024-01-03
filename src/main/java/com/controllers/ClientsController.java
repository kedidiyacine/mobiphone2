package com.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.models.Client;
import com.services.ClientService;

public class ClientsController implements Initializable {

    @FXML
    private Button btnSave;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnRefresh;

    @FXML
    private TableView<Client> tblClients;

    public ClientsController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {

            // Client client;
            // try (ClientService clientService = new ClientService()) {
            // for (int i = 0; i < 500; i++) {
            // client = new Client(i + "cin", i + "nom", i + "prenom", i + "adresse", i +
            // "email");
            // clientService.enregistrer(client);
            // }
            // } catch (SQLException e) {
            // throw e;
            // } catch (Exception e) {
            // e.printStackTrace();
            // }

            new TableController<Client, ClientService>(
                    tblClients,
                    Client.class, new ClientService(), new ActionButtons(btnSave, btnDelete, btnRefresh));
            // buttons will eventually be packaged in some data structure
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
