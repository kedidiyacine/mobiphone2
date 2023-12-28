package com.controllers;

import javafx.event.ActionEvent;
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
    private TableController<Client, ClientService> tableController;

    @FXML
    private Button btnSave;

    @FXML
    private TableView<Client> tblClients;

    public ClientsController() {
    }

    @FXML
    private void handleRefreshButton(ActionEvent event) {
        tableController.handleRefreshButton(event);
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        tableController.handleSaveButton(event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            tableController = new TableController<Client, ClientService>(
                    tblClients,
                    Client.class, new ClientService(), btnSave);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
