package com.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
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
import com.services.DataService;
import com.utils.Constants;
import com.utils.DateUtils;
import com.utils.ReflectionUtils;
import com.utils.StringUtils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TableController<T extends Identifiable<T, ?>, S extends DataService<T>> {

    private TableView<T> tableView;
    private ActionButtons actionButtons;
    private Button btnSave;
    private Button btnDelete;
    private Button btnRefresh;
    private ObservableList<T> entities = FXCollections.observableArrayList();
    private DataService<T> service;
    private final Set<String> editableColumns = new HashSet<>();
    private final Map<Serializable, Map<String, Map<String, String>>> modificationsMap = new HashMap<>();
    private final Class<T> clazz;

    private Pagination pagination;
    ReflectionUtils<T> reflectionUtils = new ReflectionUtils<>();

    private final int itemsPerPage = Constants.DEFAULT_ITEMS_PER_PAGE;

    private final Tooltip refreshTooltip = new Tooltip(Constants.REFRESH_TOOLTIP_MSG);
    private final BooleanProperty refreshing = new SimpleBooleanProperty(false);
    private final Timeline refreshThrottle = new Timeline(new KeyFrame(
            Duration.seconds(Constants.THROTTLE_DURATION),
            event -> {
                refreshing.set(false);
                refreshTooltip.hide();
                btnRefresh.setDisable(false);
            }));

    public TableController(TableView<T> tableView, Class<T> clazz, S service, ActionButtons actionButtons) {
        this.tableView = tableView;
        this.clazz = clazz;
        this.service = service;
        this.actionButtons = actionButtons;
        initializeButtons();
        initialize();
    }

    private void initializeButtons() {
        this.btnSave = actionButtons.getSaveButton();
        this.btnRefresh = actionButtons.getRefreshButton();
        this.btnDelete = actionButtons.getDeleteButton();
        actionButtons.getCreateButton();

        actionButtons.getSaveButton().setOnAction(this::handleSaveButton);
        actionButtons.getDeleteButton().setOnAction(this::handleDeleteButton);
        actionButtons.getRefreshButton().setOnAction(this::handleRefreshButton);
        actionButtons.getCreateButton().setOnAction(this::handleCreateButton);

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

    private void instructColumnCellsPopulation() {
        List<TableColumn<T, ?>> columns = getColumns();

        for (TableColumn<T, ?> column : columns) {
            if (column.getId() != null && !column.getId().isEmpty()) {
                String propertyName = column.getId();
                configureCellValueFactory(column, propertyName);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <Col> void configureCellValueFactory(TableColumn<T, Col> column, String propertyName) {
        column.setCellValueFactory(cellData -> {
            T entity = cellData.getValue();
            Object value = reflectionUtils.invokeMethod(entity, "get" +
                    StringUtils.capitalizeWord(propertyName),
                    new Class[] {}, new Object[] {});

            if (value instanceof Long) {
                return (ObservableValue<Col>) new SimpleLongProperty(Long.parseLong(value.toString()));
            } else if (value instanceof LocalDateTime) {
                return (ObservableValue<Col>) new SimpleStringProperty(
                        DateUtils.getFormattedDate((LocalDateTime) value));
            } else {
                return (ObservableValue<Col>) new SimpleStringProperty(value.toString());
            }
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

    private boolean showBulkDeleteConfirmationModal(int itemCount) {
        String contentText = "Are you sure you want to delete " + itemCount + " item(s)?";
        return showConfirmationModal(null, contentText, Constants.DELETION_MESSAGE);
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
                Class<?> propertyType = reflectionUtils.getPropertyType(clazz, columnName);
                Object convertedValue = reflectionUtils.convertToCorrectType(oldValue, propertyType);

                // Use the new setPropertyValue method
                reflectionUtils.setPropertyValue(revertedRow, columnName, convertedValue);
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

                    // Use the ReflectionUtils to get and set property values
                    Object value = reflectionUtils.invokeMethod(source, "get" +
                            StringUtils.capitalizeWord(propertyName),
                            new Class[] {}, new Object[] {});

                    reflectionUtils.setPropertyValue(copy, propertyName, value);
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

    private boolean showConfirmationModal(Serializable id, String contentText, String headerText) {

        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText(headerText);
        confirmationDialog.setContentText(contentText);

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
            Class<?> propertyType = reflectionUtils.getPropertyType(clazz, propertyName);

            // Use ReflectionUtils to set property value
            reflectionUtils.setPropertyValue(entity, propertyName,
                    reflectionUtils.convertToCorrectType(newValue, propertyType));

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
        if (modificationsMap.isEmpty()) {
            return;
        }
        String changeMessage;

        for (Map.Entry<Serializable, Map<String, Map<String, String>>> entry : modificationsMap.entrySet()) {
            Serializable id = entry.getKey();
            Map<String, Map<String, String>> columnModifications = entry.getValue();
            changeMessage = StringUtils.buildChangeMessage(id, columnModifications);

            if (showConfirmationModal(id, changeMessage, Constants.CONFIRMATION_MESSAGE)) {
                Map<String, Object> updates = new HashMap<>();

                for (Map.Entry<String, Map<String, String>> columnEntry : columnModifications.entrySet()) {
                    String columnName = columnEntry.getKey();
                    String newValue = columnEntry.getValue().get("new");

                    updates.put(columnName, newValue);
                }

                service.modifier((Long) id, updates);

            } else {
                int rowIndex = getRowIndex(id);
                if (rowIndex != -1) {
                    revertAndRefreshRow(rowIndex, columnModifications);
                }
            }
        }

        modificationsMap.clear();
    }

    @FXML
    public void handleDeleteButton(ActionEvent event) {
        TableView.TableViewSelectionModel<T> selectionModel = tableView.getSelectionModel();
        ObservableList<T> selectedItems = selectionModel.getSelectedItems();

        if (!selectedItems.isEmpty()) {
            if (showBulkDeleteConfirmationModal(selectedItems.size())) {
                ObservableList<T> newEntities = FXCollections.observableArrayList(entities);

                for (T selectedItem : selectedItems) {
                    Serializable id = selectedItem.getId();
                    service.supprimer_par_id((Long) id);
                }

                // Remove the selected items from the new modifiable list
                newEntities.removeAll(selectedItems);

                // Set the new modifiable list to entities
                entities = newEntities;

                // Refresh the items
                refreshItems();

            }
        }
    }

    private void refreshItems() {
        // Clear the tableView
        // tableView.getItems().clear();
        tableView.setItems(FXCollections.observableArrayList());

        // Update the entities list using setAll
        entities = FXCollections.observableArrayList(
                service.getAllByPage(pagination.getCurrentPageIndex() + 1, itemsPerPage));
        // Set a new modifiable ObservableList to the tableView
        tableView.setItems(entities);

        // Update the total number of items and recalculate the number of pages

        pagination.setPageCount(calculatePageCount());

    }

    @FXML
    public void handleRefreshButton(ActionEvent event) {
        if (!refreshing.get()) {
            refreshItems();
            refreshing.set(true);
            refreshThrottle.playFromStart();

            // Display the tooltip during the throttle period
            refreshTooltip.show(btnRefresh.getScene().getWindow());
        } else {
            // Optional: Provide user feedback that the refresh is in progress.
            // You can show a tooltip, disable the button, etc.
            System.out.println("Refresh is already in progress.");
        }
    }

    @FXML
    public void handleCreateButton(ActionEvent event) {
        try {
            String contentPath = Constants.CREATION_MODAL_CONTENT_PATH;
            URL resourceUrl = getClass().getResource(contentPath);
            if (resourceUrl == null) {
                System.err.println("FXML file not found: " + contentPath);
                return;
            }

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            Parent content = loader.load();

            content.setId(contentPath);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Create overlayPane and update main scene
            Pane overlayPane = createOverlayPane(currentStage);

            // Create a new stage for the modal
            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(currentStage);
            String TITLE = "Create " + clazz.getSimpleName();
            modalStage.setTitle(TITLE);

            // Dynamically create modal fields
            addModalFields(content);

            // Create and add Save button
            Button saveButton = new Button("Save");
            VBox vbox = (VBox) content.lookup("#dynamicFieldsContainer");
            if (vbox != null) {
                vbox.getChildren().add(saveButton);
            }

            // Set up listener for Save button click
            saveButton.setOnAction(saveEvent -> {
                // TODO: validation
                // build entity from Retrieved text fields values.
                try {
                    T entity = buildEntityFromModalTextFields(content);
                    service.enregistrer(entity);
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | NoSuchMethodException | SecurityException
                        | NoSuchFieldException e) {
                    e.printStackTrace();
                }
                refreshItems();

                // Close the modal
                modalStage.close();
            });

            // Set the content to the scene
            Scene modalScene = new Scene(content);
            modalStage.setScene(modalScene);

            // Add a listener to the showing property of the modal
            modalStage.showingProperty().addListener((observable, oldValue, newValue) -> {
                overlayPane.setVisible(newValue);
            });

            // Show the modal and wait for it to be closed
            modalStage.showAndWait();

            // Remove overlayPane after modal is closed
            overlayPane.setVisible(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private T buildEntityFromModalTextFields(Parent content) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException,
            NoSuchFieldException {
        T entity = clazz.getDeclaredConstructor().newInstance();

        for (String column : editableColumns) {
            TextField textField = (TextField) content.lookup("#" + column);
            if (textField != null) {
                String value = textField.getText();
                Class<?> propertyType = reflectionUtils.getPropertyType(clazz, column);
                reflectionUtils.setPropertyValue(entity, column,
                        reflectionUtils.convertToCorrectType(value, propertyType));
            }
        }
        return entity;
    }

    private Pane createOverlayPane(Stage currentStage) {
        Pane overlayPane = new Pane();
        overlayPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        overlayPane.setMouseTransparent(true);
        overlayPane.prefWidthProperty().bind(currentStage.getScene().widthProperty());
        overlayPane.prefHeightProperty().bind(currentStage.getScene().heightProperty());

        Group rootGroup = new Group(currentStage.getScene().getRoot(), overlayPane);
        Scene mainScene = new Scene(rootGroup);
        currentStage.setScene(mainScene);

        return overlayPane;
    }

    private void addModalFields(Parent content) {
        // Assuming editableColumns is a List<String> containing column names
        for (String column : editableColumns) {
            Label label = new Label(column);
            TextField textField = new TextField();

            // Assign ID to the TextField based on the column name
            textField.setId(column);

            // Add label and text field to content
            VBox vbox = (VBox) content.lookup("#dynamicFieldsContainer");
            if (vbox != null) {
                HBox hbox = new HBox(label, textField);
                vbox.getChildren().add(hbox);
            }
        }
    }

    private int calculatePageCount() {
        int totalItems = service.count();
        return (totalItems + itemsPerPage - 1) / itemsPerPage;
    }

    private Node createPage(int pageIndex) {
        entities = FXCollections.observableArrayList(
                service.getAllByPage(pageIndex, itemsPerPage));

        tableView.getItems().setAll(entities);
        return tableView; // You might need to adjust the container based on your layout
    }

    public void initialize() {
        initializeEditableColumns();
        instructColumnCellsPopulation();
        makeColumnsEditable();
        setOnEditCommitHandlersForColumns();

        // Create and configure Pagination
        int numberOfPages = calculatePageCount();
        // Add Pagination to VBox
        pagination = new Pagination(numberOfPages);
        pagination.setPageFactory(pageIndex -> createPage(pageIndex + 1));
        if (numberOfPages > 0) {
            // Get the parent of the TableView (VBox)
            Parent parent = tableView.getParent();

            // Traverse up the hierarchy until you find the VBox
            while (parent != null && !(parent instanceof VBox)) {
                parent = parent.getParent();
            }

            if (parent instanceof VBox) {
                // Add the Pagination control to the VBox
                VBox.setVgrow(pagination, Priority.ALWAYS);
                ((VBox) parent).getChildren().add(pagination);
            }
        }

        // Populate entities list before hydrating the table view
        // entities = FXCollections.observableArrayList(
        // service.getAllByPage(pagination.getCurrentPageIndex(),
        // Constants.DEFAULT_ITEMS_PER_PAGE));

        entities.addListener((ListChangeListener.Change<? extends T> change) -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    // Check if the size is below 10
                    if (entities.size() < Constants.DEFAULT_ITEMS_PER_PAGE) {
                        // Call refreshItems
                        refreshItems();
                    }
                }
            }
        });

        // tableView.getItems().addAll(entities);
        tableView.setEditable(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        btnSave.setDisable(true);
        btnDelete.disableProperty().bind(
                tableView.getSelectionModel().selectedItemProperty().isNull());

        // Set up listener to disable the refresh button during the 5-second throttle
        refreshThrottle.statusProperty().addListener((obs, oldStatus, newStatus) -> {
            if (newStatus == Timeline.Status.RUNNING) {
                // Optional: Provide user feedback that the button is disabled during throttle.
                // You can disable the button, show a tooltip, etc.
                btnRefresh.setDisable(true);
                System.out.println("Refresh button disabled during throttle.");
            }
        });
    }

}
