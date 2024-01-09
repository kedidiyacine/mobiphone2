package com.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.models.Cle3g;
import com.services.Cle3gService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class Cle3gController implements Initializable {

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
    private TableView<Cle3g> tblArticles;

    private TableController<Cle3g, Cle3gService> tableCtrl = null;

    public TableController<Cle3g, Cle3gService> getTableCtrl() {
        return tableCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // if (tableCtrl == null)
            tableCtrl = new TableController<>(tblArticles, Cle3g.class, new Cle3gService(),
                    new ActionButtons(btnSave, btnDelete, btnRefresh, btnCreate));

            ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
