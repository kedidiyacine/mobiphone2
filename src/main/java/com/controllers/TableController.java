package com.controllers;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.models.Identifiable;
import com.services.ClientService;
import com.services.DataService;
import com.utils.Constants;
import com.utils.DateUtils;
import com.utils.StringUtils;

public class TableController<T extends Identifiable<T, ?>, S extends DataService<T>> {

    private TableView<T> tableView;
    private Button btnSave;

    private ObservableList<T> entities;
    private DataService<T> service;
    private final Set<String> editableColumns = new HashSet<>();
    private final Map<Serializable, Map<String, Map<String, String>>> modificationsMap = new HashMap<>();
    private final Class<T> clazz;

    public TableController(TableView<T> tableView, Class<T> clazz, S service, Button btnSave) {
        this.tableView = tableView;
        this.clazz = clazz;
        this.service = service;
        this.btnSave = btnSave;
        initialize();
    }

    private List<TableColumn<T, ?>> getColumns() {
        return tableView.getColumns().stream().collect(Collectors.toList());
    }

    private void initializeEditableColumns() {
        List<TableColumn<T, ?>> columns = getColumns();

        if (!editableColumns.isEmpty())
            editableColumns.clear();

        for (TableColumn<T, ?> column : columns) {
            if (column.getId() != null && !column.getId().isEmpty()
                    && !Constants.NON_EDITABLE_COLUMNS.contains(column.getId())) {
                editableColumns.add(column.getId());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void instructColumnCellsPopulation() {
        List<TableColumn<T, ?>> columns = getColumns();

        for (TableColumn<T, ?> column : columns) {
            if (column.getId() != null && !column.getId().isEmpty()) {
                String propertyName = column.getId();

                String methodName = "get" +
                        propertyName.substring(0, 1).toUpperCase() +
                        propertyName.substring(1);
                try {
                    Method method = clazz.getMethod(methodName);

                    if (method.getReturnType() == LongProperty.class) {
                        configureCellValueFactory((TableColumn<T, Long>) column, method);
                    } else if (method.getReturnType() == ObjectProperty.class) {
                        configureCellValueFactory((TableColumn<T, Object>) column, method);
                    } else {
                        configureCellValueFactory((TableColumn<T, String>) column, method);
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <Col> void configureCellValueFactory(TableColumn<T, Col> column, Method method) {
        column.setCellValueFactory(cellData -> {
            try {
                Object value = method.invoke(cellData.getValue());

                if (value instanceof ObservableValue) {
                    return (ObservableValue<Col>) value;
                } else if (value instanceof LocalDateTime) {
                    return (ObservableValue<Col>) new SimpleStringProperty(
                            DateUtils.getFormattedDate((LocalDateTime) value));
                } else {
                    return (ObservableValue<Col>) new SimpleStringProperty(value.toString());
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @SuppressWarnings("unchecked")
    private void makeColumnsEditable() {
        List<TableColumn<T, ?>> columns = getColumns();

        for (TableColumn<T, ?> column : columns) {
            if (column.getId() != null && !column.getId().isEmpty() && editableColumns.contains(column.getId())) {
                ((TableColumn<T, String>) column).setCellFactory(TextFieldTableCell.forTableColumn());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void setOnEditCommitHandlersForColumns() {
        List<TableColumn<T, ?>> columns = getColumns();

        for (TableColumn<T, ?> column : columns) {
            if (column.getId() != null && !column.getId().isEmpty() && editableColumns.contains(column.getId())) {
                String propertyName = column.getId();
                ((TableColumn<T, String>) column).setOnEditCommit(event -> handleEditCommit(event, propertyName));
            }
        }
    }

    private void hydrateTableView(ObservableList<T> entities) {
        tableView.getItems().addAll(entities);
        tableView.setEditable(true);

        instructColumnCellsPopulation();
        makeColumnsEditable();
        setOnEditCommitHandlersForColumns();
    }

    private void saveChanges() {
        if (modificationsMap.isEmpty()) {
            return;
        }

        for (Map.Entry<Serializable, Map<String, Map<String, String>>> entry : modificationsMap.entrySet()) {
            Serializable id = entry.getKey();
            Map<String, Map<String, String>> columnModifications = entry.getValue();

            if (showConfirmationModal(id, columnModifications)) {
                Map<String, Object> updates = new HashMap<>();

                for (Map.Entry<String, Map<String, String>> columnEntry : columnModifications.entrySet()) {
                    String columnName = columnEntry.getKey();
                    String newValue = columnEntry.getValue().get("new");

                    updates.put(columnName, newValue);
                }

                if (service instanceof ClientService) {
                    ((ClientService) service).modifier((Long) id, updates);
                }
            } else {
                int rowIndex = getRowIndex(id);
                if (rowIndex != -1) {
                    revertAndRefreshRow(rowIndex, columnModifications);
                }
            }
        }

        modificationsMap.clear();
    }

    private void revertAndRefreshRow(int rowIndex, Map<String, Map<String, String>> columnModifications) {
        T originalRow = tableView.getItems().get(rowIndex);
        T revertedRow = revertChanges(columnModifications, originalRow);
        tableView.getItems().set(rowIndex, revertedRow);
    }

    private T revertChanges(Map<String, Map<String, String>> columnModifications, T originalRow) {
        T revertedRow = copyProperties(originalRow);

        for (Map.Entry<String, Map<String, String>> columnEntry : columnModifications.entrySet()) {
            String columnName = columnEntry.getKey();
            String oldValue = columnEntry.getValue().get("old");

            try {
                Method method = clazz.getMethod("set" + columnName.substring(0, 1).toUpperCase() +
                        columnName.substring(1), String.class);
                method.invoke(revertedRow, oldValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return revertedRow;
    }

    private T copyProperties(T source) {
        try {
            T copy = clazz.getDeclaredConstructor().newInstance();

            for (TableColumn<T, ?> column : getColumns()) {
                if (column.getId() != null && !column.getId().isEmpty()) {
                    String propertyName = column.getId();

                    String getMethodName = "get" +
                            propertyName.substring(0, 1).toUpperCase() +
                            propertyName.substring(1);

                    String setMethodName = "set" +
                            propertyName.substring(0, 1).toUpperCase() +
                            propertyName.substring(1);

                    Method getMethod = clazz.getMethod(getMethodName);
                    Method setMethod = clazz.getMethod(setMethodName, getMethod.getReturnType());

                    Object value = getMethod.invoke(source);
                    setMethod.invoke(copy, value);
                }
            }

            return copy;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private <K extends Serializable> int getRowIndex(K id) {
        for (int i = 0; i < tableView.getItems().size(); i++) {
            if (Objects.equals(tableView.getItems().get(i).getId(), id)) {
                return i;
            }
        }
        return -1;
    }

    private boolean showConfirmationModal(Serializable id, Map<String, Map<String, String>> columnModifications) {
        String changeMessage = StringUtils.buildChangeMessage(id, columnModifications);

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

    private <K extends Serializable> void addToModificationsMap(K key, String propertyName, String oldValue,
            String newValue) {
        modificationsMap.computeIfAbsent(key, k -> new HashMap<>())
                .put(propertyName, Map.of("old", oldValue, "new", newValue));
    }

    private void handleEditCommit(TableColumn.CellEditEvent<T, String> event, String propertyName) {
        T entity = event.getRowValue();
        String oldValue = event.getOldValue();
        String newValue = event.getNewValue();

        Serializable entityId = entity.getId();
        addToModificationsMap(entityId, propertyName, oldValue, newValue);

        try {
            Method method = clazz.getMethod("set" + propertyName.substring(0, 1).toUpperCase() +
                    propertyName.substring(1), String.class);
            method.invoke(entity, newValue);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (btnSave.isDisabled()) {
            btnSave.setDisable(false);
        }
    }

    @FXML
    public void handleSaveButton(ActionEvent event) {
        if (!btnSave.isDisabled())
            btnSave.setDisable(true);
        saveChanges();
    }

    @FXML
    public void handleRefreshButton(ActionEvent event) {
        entities = FXCollections.observableArrayList(service.getAllByPage(1, 10));

        tableView.getItems().clear();
        tableView.getItems().addAll(entities);
    }

    public void initialize() {
        entities = FXCollections.observableArrayList(service.getAllByPage(1, 10));

        tableView.getItems().clear();
        btnSave.setDisable(true);

        initializeEditableColumns();

        hydrateTableView(entities);
    }
}
