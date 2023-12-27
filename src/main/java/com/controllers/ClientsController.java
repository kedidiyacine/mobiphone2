package com.controllers;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.models.Client;
import com.services.ClientService;
import com.utils.Constants;
import com.utils.StringUtils;
import com.utils.DateUtils;

public class ClientsController extends TableController<Client, ClientService> {

    private ClientService clientService;
    private ObservableList<Client> clients;
    private Map<Long, Map<String, Map<String, String>>> modificationsMap = new HashMap<>();
    private final Set<String> editableColumns = new HashSet<>();

    @FXML
    private TableView<Client> tblClients;
    @FXML
    private Button btnSave;

    public ClientsController() throws SQLException {
        super(new TableView<Client>(), Client.class, new ClientService());
        clientService = new ClientService();
    }

    private void initializeEditableColumns() {
        List<TableColumn<Client, ?>> columns = getColumns();

        // Clear existing entries in case I want to reset the editable columns
        if (!editableColumns.isEmpty())
            editableColumns.clear();

        for (TableColumn<Client, ?> column : columns) {
            if (column.getId() != null && !column.getId().isEmpty()
                    && !Constants.NON_EDITABLE_COLUMNS.contains(column.getId())) {
                editableColumns.add(column.getId());
            }
        }
    }

    private List<TableColumn<Client, ?>> getColumns() {
        return tblClients.getColumns().stream().collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private void instructColumnCellsPopulation() {
        List<TableColumn<Client, ?>> columns = getColumns();

        for (TableColumn<Client, ?> column : columns) {
            if (column.getId() != null && !column.getId().isEmpty()) {
                String propertyName = column.getId();

                // if I want to change the column names, the changes need to be reflected here.
                // to get the proper class getter for the property
                String methodName = "get" +
                        propertyName.substring(0, 1).toUpperCase() +
                        propertyName.substring(1);
                try {
                    Method method = Client.class.getMethod(methodName);

                    if (method.getReturnType() == LongProperty.class) {
                        configureCellValueFactory((TableColumn<Client, Long>) column, method);
                    } else if (method.getReturnType() == ObjectProperty.class) {
                        configureCellValueFactory((TableColumn<Client, Object>) column, method);
                    } else {
                        // Assuming other types are StringProperty
                        configureCellValueFactory((TableColumn<Client, String>) column, method);
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void configureCellValueFactory(TableColumn<Client, T> column, Method method) {
        column.setCellValueFactory(cellData -> {
            try {
                Object value = method.invoke(cellData.getValue());

                if (value instanceof ObservableValue) {
                    return (ObservableValue<T>) value;
                } else if (value instanceof LocalDateTime) {
                    return (ObservableValue<T>) new SimpleStringProperty(
                            DateUtils.getFormattedDate((LocalDateTime) value));
                } else {
                    return (ObservableValue<T>) new SimpleStringProperty(value.toString());
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @SuppressWarnings("unchecked")
    private void makeColumnsEditable() {
        List<TableColumn<Client, ?>> columns = getColumns();

        for (TableColumn<Client, ?> column : columns) {
            if (column.getId() != null && !column.getId().isEmpty() && editableColumns.contains(column.getId())) {
                ((TableColumn<Client, String>) column).setCellFactory(TextFieldTableCell.forTableColumn());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void setOnEditCommitHandlersForColumns() {
        List<TableColumn<Client, ?>> columns = getColumns();

        for (TableColumn<Client, ?> column : columns) {
            if (column.getId() != null && !column.getId().isEmpty() && editableColumns.contains(column.getId())) {
                String propertyName = column.getId();
                ((TableColumn<Client, String>) column).setOnEditCommit(event -> handleEditCommit(event, propertyName));
            }
        }
    }

    private void hydrateClientsTableView(ObservableList<Client> clients) {
        tblClients.getItems().addAll(clients);
        tblClients.setEditable(true);

        instructColumnCellsPopulation();
        makeColumnsEditable();
        setOnEditCommitHandlersForColumns();
    }

    @FXML
    private void saveChanges() {
        if (modificationsMap.size() == 0) {
            return;
        }

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
        Client revertedRow = copyClientProperties(originalRow);

        for (Map.Entry<String, Map<String, String>> columnEntry : columnModifications.entrySet()) {
            String columnName = columnEntry.getKey();
            String oldValue = columnEntry.getValue().get("old");

            try {
                Method method = Client.class.getMethod("set" + columnName.substring(0, 1).toUpperCase() +
                        columnName.substring(1), String.class);
                method.invoke(revertedRow, oldValue);
            } catch (Exception e) {
                e.printStackTrace();
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

        Alert confirmationDialog = new Alert(AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText(Constants.CONFIRMATION_MESSAGE);
        confirmationDialog.setContentText(changeMessage);

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        confirmationDialog.getButtonTypes().setAll(yesButton, cancelButton);

        Optional<ButtonType> result = confirmationDialog.showAndWait();
        return result.isPresent() && result.get() == yesButton;
    }

    private void handleEditCommit(TableColumn.CellEditEvent<Client, String> event, String propertyName) {
        Client client = event.getRowValue();
        String oldValue = event.getOldValue();
        String newValue = event.getNewValue();

        modificationsMap.computeIfAbsent(client.getId(), k -> new HashMap<>())
                .put(propertyName, Map.of("old", oldValue, "new", newValue));

        try {
            Method method = Client.class.getMethod("set" + propertyName.substring(0, 1).toUpperCase() +
                    propertyName.substring(1), String.class);
            method.invoke(client, newValue);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (btnSave.isDisabled())
            btnSave.setDisable(false);
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        if (!btnSave.isDisabled())
            btnSave.setDisable(true);
        saveChanges();
    }

    @FXML
    private void handleRefreshButton(ActionEvent event) {
        clients = FXCollections.observableArrayList(clientService.getAllByPage(1, 10));
        tblClients.getItems().clear();
        tblClients.getItems().addAll(clients);
    }

    public void initialize(URL location, ResourceBundle resources) {
        clients = FXCollections.observableArrayList(clientService.getAllByPage(1, 10));
        tblClients.getItems().clear();
        btnSave.setDisable(true);

        // Initialize editable columns
        initializeEditableColumns();

        hydrateClientsTableView(clients);
    }
}
