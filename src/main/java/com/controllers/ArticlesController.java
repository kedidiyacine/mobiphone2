package com.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.scene.control.TableView;

import com.models.Client;
import com.models.TelephoneMobile;
import com.services.ClientService;

import javafx.event.ActionEvent;

// import com.services.TelephoneMobileService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class ArticlesController implements Initializable {

    // private TableController<TelephoneMobile, TelephoneMobileService>
    // tableController;

    @FXML
    private Button btnSave;

    @FXML
    private TableView<TelephoneMobile> tblArticles;

    @FXML
    private void handleRefreshButton(ActionEvent event) {
        // tableController.handleRefreshButton(event);
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        // tableController.handleSaveButton(event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // try {
        // tableController = new TableController<TelephoneMobile,
        // TelephoneMobileService>(
        // tblArticles,
        // TelephoneMobile.class, new TelephoneMobileService(), btnSave);
        // } catch (SQLException e) {
        // e.printStackTrace();
        // }

    }

}
