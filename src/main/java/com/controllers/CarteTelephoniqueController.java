package com.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.models.CarteTelephonique;
import com.services.CarteTelephoniqueService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class CarteTelephoniqueController implements Initializable {

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
    private TableView<CarteTelephonique> tblArticles;

    private TableController<CarteTelephonique, CarteTelephoniqueService> tableCtrl = null;

    public TableController<CarteTelephonique, CarteTelephoniqueService> getTableCtrl() {
        return tableCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // if (tableCtrl == null)
            tableCtrl = new TableController<>(tblArticles, CarteTelephonique.class, new CarteTelephoniqueService(),
                    new ActionButtons(btnSave, btnDelete, btnRefresh, btnCreate));

            ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
