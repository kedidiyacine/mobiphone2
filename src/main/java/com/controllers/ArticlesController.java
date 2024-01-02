package com.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.models.TelephoneMobile;
import com.services.TelephoneMobileService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;

public class ArticlesController implements Initializable {
    public List<String> ARTICLE_TYPES = Arrays.asList("telephone mobile", "ligne telephone", "carte telephone",
            "cle 3g");
    private String selectedType = "telephone mobile";

    @FXML
    private ComboBox<String> cmbArticleType;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnRefresh;

    @FXML
    private TableView<TelephoneMobile> tblArticles;

    @FXML
    private void handleArticleTypeChange(ActionEvent event) throws SQLException {
        selectedType = cmbArticleType.getValue();
        if (selectedType != null) {
            updateTableController(selectedType);
        }
    }

    private void updateTableController(String articleType) throws SQLException {
        ActionButtons actionButtons = new ActionButtons(btnSave, btnDelete, btnRefresh);

        switch (articleType) {
            case "telephone mobile":
                new TableController<TelephoneMobile, TelephoneMobileService>(
                        tblArticles, TelephoneMobile.class, new TelephoneMobileService(), actionButtons);
                break;
            // Add cases for other article types if needed
            default:
                // Handle unknown type or provide a default behavior
                break;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            cmbArticleType.getItems().addAll(ARTICLE_TYPES);
            cmbArticleType.getSelectionModel().selectFirst();
            selectedType = cmbArticleType.getValue();
            updateTableController(selectedType);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
