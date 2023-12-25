package com.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.LongStringConverter;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;

import com.models.Client;
import com.services.ClientService;
import com.utils.DateUtils;
import com.db.DatabaseUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class ClientsController implements Initializable {
    private static ClientService clientService;
    private static Connection connection;
    @FXML
    private TableView<Client> tblClients;
    @FXML
    private TableColumn<Client, Long> colId;
    @FXML
    private TableColumn<Client, String> colAdresseLivraison;
    @FXML
    private TableColumn<Client, String> colCin;
    @FXML
    private TableColumn<Client, String> colEmail;
    @FXML
    private TableColumn<Client, String> colMaj;
    @FXML
    private TableColumn<Client, String> colCreation;
    @FXML
    private TableColumn<Client, String> colNom;
    @FXML
    private TableColumn<Client, String> colPrenom;

    public ClientsController() {
        try {
            connection = DatabaseUtil.getConnection();
            clientService = new ClientService(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void refreshTableRow(TableView<Client> tableView, int rowIndex) {
        tableView.getItems().set(rowIndex, tableView.getItems().get(rowIndex));
    }

    public void initialize(URL location, ResourceBundle resources) {
        List<Client> clients = clientService.getAllByPage(1, 10);

        if (tblClients != null) {
            tblClients.getItems().addAll(clients);

            tblClients.setEditable(true);

            // Make the colCin column editable
            // colCin.setCellValueFactory(new PropertyValueFactory<>("cin"));
            colCin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCin()));
            colCin.setCellFactory(TextFieldTableCell.forTableColumn()); // Explicitly set the converter
            colCin.setOnEditCommit(event -> {
                Client client = event.getRowValue();

                String columnName = "cin"; // Set the actual column name dynamically based on your TableColumn

                Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationDialog.setTitle("Confirmation");
                confirmationDialog.setHeaderText("Are you sure you want to confirm these changes?");

                // Build the message showing the changes
                String changeMessage = String.format("Change %s from '%s' to '%s'", columnName, event.getOldValue(),
                        event.getNewValue());

                confirmationDialog.setContentText(changeMessage);

                ButtonType yesButton = new ButtonType("Yes");
                ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                confirmationDialog.getButtonTypes().setAll(yesButton, cancelButton);

                Optional<ButtonType> result = confirmationDialog.showAndWait();

                if (result.isPresent() && result.get() == yesButton) {
                    client.setCin(event.getNewValue());
                    // User clicked "Yes," proceed with the modification
                    Map<String, Object> updates = new HashMap<>();
                    updates.put(columnName, client.getCin());
                    clientService.modifier(client.getId(), updates);
                } else {
                    // User clicked "Cancel," refresh the entire row
                    refreshTableRow(tblClients, event.getTablePosition().getRow());
                }
            });

            colId.setCellValueFactory(cellData -> new SimpleObjectProperty<Long>(cellData.getValue().getId()));
            colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
            colPrenom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrenom()));
            colAdresseLivraison.setCellValueFactory(
                    cellData -> new SimpleStringProperty(cellData.getValue().getAdresse_de_livraison()));
            colEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
            colCreation.setCellValueFactory(
                    cellData -> new SimpleStringProperty(
                            DateUtils.getFormattedDate(cellData.getValue().getDate_creation())));
            colMaj.setCellValueFactory(cellData -> new SimpleStringProperty(
                    DateUtils.getFormattedDate(cellData.getValue().getDate_maj())));
        } else {
            System.out.println("Table View is empty");
        }
    }

}