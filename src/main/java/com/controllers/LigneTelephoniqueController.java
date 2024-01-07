package com.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.models.LigneTelephonique;
import com.services.LigneTelephoniqueService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class LigneTelephoniqueController implements Initializable {

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
    private TableView<LigneTelephonique> tblArticles;

    private TableController<LigneTelephonique, LigneTelephoniqueService> tableCtrl = null;

    public TableController<LigneTelephonique, LigneTelephoniqueService> getTableCtrl() {
        return tableCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // if (tableCtrl == null)
        try {
            tableCtrl = new TableController<>(tblArticles,
                    LigneTelephonique.class,
                    new LigneTelephoniqueService(),
                    new ActionButtons(btnSave, btnDelete, btnRefresh, btnCreate));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
