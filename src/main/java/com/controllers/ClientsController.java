package com.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import com.utils.Constants;
import com.utils.DateUtils;
import com.utils.StringUtils;
import com.db.DatabaseUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class ClientsController implements Initializable, AutoCloseable {
    private ClientService clientService;
    private Connection connection;
    private ObservableList<Client> clients;
    private Map<Long, Map<String, Map<String, String>>> modificationsMap = new HashMap<>();

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

    private void instructColumnCellsPopulation() {
        colId.setCellValueFactory(cellData -> new SimpleObjectProperty<Long>(cellData.getValue().getId()));
        // colCin.setCellValueFactory(new PropertyValueFactory<>("cin"));
        colCin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCin()));
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
    }

    private void makeColumnsEditable() {
        colCin.setCellFactory(TextFieldTableCell.forTableColumn());

        colNom.setCellFactory(TextFieldTableCell.forTableColumn());

        colPrenom.setCellFactory(TextFieldTableCell.forTableColumn());

        colAdresseLivraison.setCellFactory(TextFieldTableCell.forTableColumn());

        colEmail.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    private void setOnEditCommitHandlersForColumns() {
        colCin.setOnEditCommit(event -> handleEditCommit(event, "cin"));

        colNom.setOnEditCommit(event -> handleEditCommit(event, "nom"));

        colPrenom.setOnEditCommit(event -> handleEditCommit(event, "prenom"));

        colAdresseLivraison.setOnEditCommit(event -> handleEditCommit(event, "adresse_de_livraison"));

        colEmail.setOnEditCommit(event -> handleEditCommit(event, "email"));
    }

    private void hydrateClientsTableView(ObservableList<Client> clients) {
        tblClients.getItems().addAll(clients);
        tblClients.setEditable(true);

        // specify how to populate all cells within each single TableColumn
        instructColumnCellsPopulation();

        // Make some columns editable
        makeColumnsEditable();

        setOnEditCommitHandlersForColumns();

    }

    private void handleEditCommit(TableColumn.CellEditEvent<Client, String> event, String columnName) {
        Client client = event.getRowValue();
        String oldValue = event.getOldValue();
        String newValue = event.getNewValue();

        modificationsMap.computeIfAbsent(client.getId(), k -> new HashMap<>())
                .put(columnName, Map.of("old", oldValue, "new", newValue));

        switch (columnName) {
            case "cin" -> client.setCin(newValue);
            case "nom" -> client.setNom(newValue);
            case "prenom" -> client.setPrenom(newValue);
            case "adresse_de_livraison" -> client.setAdresse_de_livraison(newValue);
            case "email" -> client.setEmail(newValue);
        }
    }

    @FXML
    private void saveChanges() {
        for (Map.Entry<Long, Map<String, Map<String, String>>> entry : modificationsMap.entrySet()) {
            Long clientId = entry.getKey();
            Map<String, Map<String, String>> columnModifications = entry.getValue();

            if (showConfirmationModal(clientId, columnModifications)) {
                Map<String, Object> updates = new HashMap<>();

                for (Map.Entry<String, Map<String, String>> columnEntry : columnModifications.entrySet()) {
                    String columnName = columnEntry.getKey();
                    String newValue = columnEntry.getValue().get("new");

                    updates.put(columnName, newValue);
                }

                clientService.modifier(clientId, updates);
            } else {
                int rowIndex = getClientRowIndex(clientId);
                if (rowIndex != -1) {
                    revertAndRefreshRow(rowIndex, columnModifications);
                }
            }
        }

        modificationsMap.clear();
    }

    private void revertAndRefreshRow(int rowIndex, Map<String, Map<String, String>> columnModifications) {
        Client originalRow = tblClients.getItems().get(rowIndex);
        Client revertedRow = revertChanges(columnModifications, originalRow);
        tblClients.getItems().set(rowIndex, revertedRow);
    }

    private Client revertChanges(Map<String, Map<String, String>> columnModifications, Client originalRow) {
        // Create a copy of the original row
        Client revertedRow = copyClientProperties(originalRow);

        // Set properties based on columnModifications
        for (Map.Entry<String, Map<String, String>> columnEntry : columnModifications.entrySet()) {
            String columnName = columnEntry.getKey();
            String oldValue = columnEntry.getValue().get("old");

            switch (columnName) {
                case "cin" -> revertedRow.setCin(oldValue);
                case "nom" -> revertedRow.setNom(oldValue);
                case "prenom" -> revertedRow.setPrenom(oldValue);
                case "adresse_de_livraison" -> revertedRow.setAdresse_de_livraison(oldValue);
                case "email" -> revertedRow.setEmail(oldValue);
                // Add cases for other columns as needed
            }
        }

        return revertedRow;
    }

    private Client copyClientProperties(Client source) {
        Client copy = new Client();
        copy.setId(source.getId());
        copy.setCin(source.getCin());
        copy.setNom(source.getNom());
        copy.setPrenom(source.getPrenom());
        copy.setAdresse_de_livraison(source.getAdresse_de_livraison());
        copy.setEmail(source.getEmail());
        copy.setDate_creation(source.getDate_creation());
        copy.setDate_maj(source.getDate_maj());
        return copy;
    }

    private int getClientRowIndex(Long clientId) {
        for (int i = 0; i < tblClients.getItems().size(); i++) {
            if (Objects.equals(tblClients.getItems().get(i).getId(), clientId)) {
                return i;
            }
        }
        return -1;
    }

    private boolean showConfirmationModal(Long clientId, Map<String, Map<String, String>> columnModifications) {
        String changeMessage = StringUtils.buildChangeMessage(clientId, columnModifications);

        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText(Constants.CONFIRMATION_MESSAGE);
        confirmationDialog.setContentText(changeMessage);

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmationDialog.getButtonTypes().setAll(yesButton, cancelButton);

        Optional<ButtonType> result = confirmationDialog.showAndWait();
        return result.isPresent() && result.get() == yesButton;
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        // Implement the logic to save changes
        saveChanges();
    }

    @FXML
    private void handleRefreshButton(ActionEvent event) {
        // Fetch the updated data from the database
        clients = FXCollections.observableArrayList(clientService.getAllByPage(1, 10));

        // Clear the existing data and add the updated data
        tblClients.getItems().clear();
        tblClients.getItems().addAll(clients);
    }

    // private void refreshTableRow(TableView<Client> tableView, int rowIndex) {
    // tableView.getItems().set(rowIndex, tableView.getItems().get(rowIndex));
    // }

    @Override
    public void close() throws Exception {
        connection.close();
    }

    public void initialize(URL location, ResourceBundle resources) {

        clients = FXCollections.observableArrayList(clientService.getAllByPage(1, 10));

        // Clear the existing data and add the updated data
        tblClients.getItems().clear();

        hydrateClientsTableView(clients);
    }

}