package com.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.models.TelephoneMobile;
import com.services.TelephoneMobileService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class TelephoneMobileController implements Initializable {

    @FXML
    private VBox root;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnRefresh;

    @FXML
    private Button btnCreate;

    @FXML
    private TableView<TelephoneMobile> tblArticles;

    private TableController<TelephoneMobile, TelephoneMobileService> tableCtrl = null;

    public TableController<TelephoneMobile, TelephoneMobileService> getTableCtrl() {
        return tableCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // if (tableCtrl == null)
            tableCtrl = new TableController<>(tblArticles, TelephoneMobile.class, new TelephoneMobileService(),
                    new ActionButtons(btnSave, btnDelete, btnRefresh, btnCreate));

            ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
