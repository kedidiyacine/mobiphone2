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
    private Button btnCreate;

    @FXML
    private TableView<Client> tblClients;

    public ClientsController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            new TableController<Client, ClientService>(tblClients,
                    Client.class,
                    new ClientService(),
                    new ActionButtons(btnSave, btnDelete, btnRefresh, btnCreate));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
