package com.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.models.Identifiable;
import com.services.DataService;
import com.utils.Constants;
import com.utils.MyStringConverter;
import com.utils.ReflectionUtils;
import com.utils.StringUtils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * TableController manages a JavaFX TableView for displaying and interacting
 * with data of a specific type T. It integrates with a DataService for CRUD
 * operations, provides a set of action buttons, and includes features like
 * pagination, editing, deleting, refreshing, and creating records.
 *
 * @param <T> The type of entities displayed in the TableView, must extend
 *            Identifiable.
 * @param <S> The DataService type for CRUD operations on entities of type T.
 */
public class TableController<T extends Identifiable<T, ?>, S extends DataService<T>> {

    private TableView<T> tableView;
    private DataService<T> service;
    private final Class<T> clazz;
    private ActionButtons actionButtons;
    private Button btnSave;
    private Button btnDelete;
    private Button btnRefresh;
    private ObservableList<T> entities = FXCollections.observableArrayList();
    private final Set<String> editableColumns = new HashSet<>();
    private final Map<Serializable, Map<String, Map<String, ?>>> modificationsMap = new HashMap<>();
    private final ReflectionUtils reflection = new ReflectionUtils();

    private Pagination pagination;

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

    /**
     * Constructs a TableController with the specified TableView, entity type,
     * DataService,
     * and action buttons for CRUD operations.
     *
     * @param tableView     The JavaFX TableView for displaying entities.
     * @param clazz         The Class type of entities.
     * @param service       The DataService for CRUD operations on entities.
     * @param actionButtons ActionButtons containing Save, Delete, Refresh, and
     *                      Create buttons.
     */
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

    private ObservableList<TableColumn<T, ?>> getColumns() {
        return tableView.getColumns();
    }

    /**
     * Initializes editable columns based on the TableColumn IDs.
     */
    private void initializeEditableColumns() {
        ObservableList<TableColumn<T, ?>> columns = getColumns();

        if (!editableColumns.isEmpty())
            editableColumns.clear();

        for (TableColumn<T, ?> column : columns) {
            if (column.getId() != null && !column.getId().isEmpty()
                    && !Constants.NON_EDITABLE_COLUMNS.contains(column.getId())) {
                editableColumns.add(column.getId());
            }
        }
    }

    /**
     * Configures the CellValueFactory for each column based on their IDs.
     */
    private void instructColumnCellsPopulation() {
        ObservableList<TableColumn<T, ?>> columns = getColumns();

        for (TableColumn<T, ?> column : columns) {
            if (column.getId() != null && !column.getId().isEmpty()) {
                String propertyName = column.getId();
                configureCellValueFactory(column, propertyName);
            }
        }
    }

    private <Col> void configureCellValueFactory(TableColumn<T, Col> column, String propertyName) {
        PropertyValueFactory<T, Col> valueFactory = new PropertyValueFactory<>(propertyName);

        column.setCellValueFactory(valueFactory);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void makeColumnsEditable() {
        ObservableList<TableColumn<T, ?>> columns = getColumns();

        for (TableColumn<T, ?> column : columns) {
            if (column.getId() != null && !column.getId().isEmpty() && editableColumns.contains(column.getId())) {
                MyStringConverter<?> converter = new MyStringConverter<>(column);
                if (converter != null) {
                    setCellFactoryForColumn(column, (StringConverter) converter);
                }
            }
        }
    }

    /**
     * Configures columns to be editable and uses TextFieldTableCell for editing.
     */
    private <Col> void setCellFactoryForColumn(TableColumn<T, Col> column, StringConverter<Col> converter) {
        column.setCellFactory(TextFieldTableCell.forTableColumn(converter));
    }

    private void setOnEditCommitHandlersForColumns() {
        ObservableList<TableColumn<T, ?>> columns = getColumns();

        for (TableColumn<T, ?> column : columns) {
            if (column.getId() != null && !column.getId().isEmpty() && editableColumns.contains(column.getId())) {
                String propertyName = column.getId();
                setOnEditCommitHandler(column, propertyName);
            }
        }
    }

    /**
     * Sets the OnEditCommit handler for editable columns.
     *
     * @param <Col>        The type of the TableColumn.
     * @param column       The TableColumn for which to set the handler.
     * @param propertyName The property name associated with the TableColumn.
     */
    private <Col> void setOnEditCommitHandler(TableColumn<T, Col> column, String propertyName) {
        column.setOnEditCommit(event -> handleEditCommit(event));
    }

    /**
     * Shows a confirmation dialog for bulk deletion and initiates the deletion
     * process.
     *
     * @param itemCount The number of items selected for deletion.
     * @return True if the user confirms the deletion, false otherwise.
     */
    private boolean showBulkDeleteConfirmationModal(int itemCount) {
        String headerText = StringUtils.buildDeletionMessage(itemCount);
        return showConfirmationModal(null, headerText, Constants.DELETION_MESSAGE_CONTENT);
    }

    private void revertAndRefreshRow(int rowIndex, Map<String, Map<String, ?>> columnModifications) {
        T originalRow = tableView.getItems().get(rowIndex);
        T revertedRow = revertChanges(columnModifications, originalRow);
        tableView.getItems().set(rowIndex, revertedRow);
    }

    private T revertChanges(Map<String, Map<String, ?>> columnModifications, T originalRow) {
        T revertedRow = copyProperties(originalRow);

        for (Map.Entry<String, Map<String, ?>> columnEntry : columnModifications.entrySet()) {
            String columnName = columnEntry.getKey();

            try {
                // Use the new setPropertyValue method
                reflection.setPropertyValue(revertedRow, columnName, columnEntry.getValue().get("old"));
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
                    Object value = reflection.invokeMethod(source, "get" +
                            StringUtils.capitalizeWord(propertyName),
                            new Class[] {}, new Object[] {});

                    reflection.setPropertyValue(copy, propertyName, value);
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

    private boolean showConfirmationModal(Serializable id, String headerText, String contentText) {

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

    private <K extends Serializable, Col> void addToModificationsMap(K key, String propertyName, Col oldValue,
            Col newValue) {
        modificationsMap.computeIfAbsent(key, k -> new HashMap<>())
                .put(propertyName, Map.of("old", oldValue, "new", newValue));
    }

    /**
     * Handles the OnEditCommit event when a cell value is edited.
     *
     * @param <Col> The type of the TableColumn.
     * @param event The CellEditEvent containing information about the edit.
     */
    private <Col> void handleEditCommit(TableColumn.CellEditEvent<T, Col> event) {
        T entity = event.getRowValue();
        Col oldValue = event.getOldValue();
        Col newValue = event.getNewValue();

        Serializable entityId = entity.getId();
        addToModificationsMap(entityId, event.getTableColumn().getId(), oldValue, newValue);

        try {
            String propertyName = event.getTableColumn().getId();
            // Use TableColumn's getCellData method to get the current value
            reflection.setPropertyValue(entity, propertyName, event.getTableColumn().getCellData(entity));

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (btnSave.isDisabled()) {
            btnSave.setDisable(false);
        }
    }

    /**
     * Handles the Save button action by persisting the pending modifications.
     *
     * @param event The ActionEvent triggered by the Save button.
     */
    @FXML
    public void handleSaveButton(ActionEvent event) {
        if (!btnSave.isDisabled())
            btnSave.setDisable(true);
        if (modificationsMap.isEmpty()) {
            return;
        }
        String changeMessage;

        for (Map.Entry<Serializable, Map<String, Map<String, ?>>> entry : modificationsMap.entrySet()) {
            Serializable id = entry.getKey();
            Map<String, Map<String, ?>> columnModifications = entry.getValue();
            changeMessage = StringUtils.buildChangeMessage(id, columnModifications);

            if (showConfirmationModal(id, changeMessage, Constants.CONFIRMATION_MESSAGE)) {
                Map<String, Object> updates = new HashMap<>();

                for (Map.Entry<String, Map<String, ?>> columnEntry : columnModifications.entrySet()) {
                    String columnName = columnEntry.getKey();

                    updates.put(columnName, columnEntry.getValue().get("new"));
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

    /**
     * Handles the Delete button action by initiating the deletion process for
     * selected items.
     *
     * @param event The ActionEvent triggered by the Delete button.
     */
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
        tableView.setItems(FXCollections.observableArrayList());

        // Update the entities list from the datasource
        entities = FXCollections.observableArrayList(
                service.getAllByPage(pagination.getCurrentPageIndex() + 1, itemsPerPage));
        // Set a new modifiable ObservableList to the tableView
        tableView.setItems(entities);

        // Update the total number of items and recalculate the number of pages
        pagination.setPageCount(calculatePageCount());

    }

    /**
     * Handles the Refresh button action by refreshing the displayed data.
     *
     * @param event The ActionEvent triggered by the Refresh button.
     */
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

    /**
     * Handles the Create button action by opening a modal for creating new records.
     *
     * @param event The ActionEvent triggered by the Create button.
     */
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
            modalStage.initModality(Modality.WINDOW_MODAL);
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

    /**
     * Builds an entity object from the values entered in the modal's text fields.
     *
     * @param content The Parent representing the content of the creation modal.
     * @return The entity object created from the modal text fields.
     */
    private T buildEntityFromModalTextFields(Parent content) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException,
            NoSuchFieldException {
        T entity = clazz.getDeclaredConstructor().newInstance();

        ObservableList<TableColumn<T, ?>> columns = getColumns();

        for (TableColumn<T, ?> column : columns) {
            String columnName = column.getId();
            if (editableColumns.contains(columnName)) {
                Object cellValue = getCellValueFromCellFactory(content, column);

                if (cellValue != null) {
                    reflection.setPropertyValue(entity, columnName,
                            cellValue);
                }
            }
        }

        return entity;
    }

    /**
     * Gets the cell value from the specified column's cell factory.
     *
     * @param <Col>   The type of the TableColumn.
     * @param content The Parent representing the content of the modal.
     * @param column  The TableColumn for which to get the cell value.
     * @return The cell value retrieved from the specified column's cell factory.
     */
    @SuppressWarnings("unchecked")
    private <Col> Col getCellValueFromCellFactory(Parent content, TableColumn<T, Col> column) {
        Callback<?, ?> cellFactory = column.getCellFactory();

        if (cellFactory instanceof Callback) {
            Callback<T, TableCell<T, Col>> cellFactoryCallback = (Callback<T, TableCell<T, Col>>) cellFactory;

            // Create a temporary TableCell to retrieve the StringConverter
            TableCell<T, Col> tableCell = cellFactoryCallback.call(null);

            if (tableCell != null) {
                String textFieldId = column.getId();
                TextField textField = (TextField) content.lookup("#" + textFieldId);

                if (textField != null) {
                    // Use the StringConverter associated with the TableColumn
                    StringConverter<Col> converter = getConverterForColumn(column);

                    if (converter != null) {
                        // Convert the text from the TextField to the appropriate type
                        return converter.fromString(textField.getText());
                    }
                }
            }
        }
        return null;
    }

    /**
     * Creates an overlay pane to prevent interaction with the main stage during the
     * modal display.
     *
     * @param currentStage The current main stage of the application.
     * @return The overlay pane created for the modal display.
     */
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

    /**
     * Adds modal fields dynamically based on editable columns.
     *
     * @param content The Parent representing the content of the creation modal.
     */
    private void addModalFields(Parent content) {
        ObservableList<TableColumn<T, ?>> columns = getColumns();

        for (TableColumn<T, ?> column : columns) {
            String columnName = column.getId();
            if (editableColumns.contains(columnName)) {
                Label label = new Label(column.getText());
                TextField textField = new TextField();
                textField.setId(columnName);

                // Use the StringConverter associated with the TableColumn
                StringConverter<?> converter = getConverterForColumn(column);

                if (converter != null) {
                    // Set the converter for the TextField
                    textField.setTextFormatter(new TextFormatter<>(converter));
                }

                VBox vbox = (VBox) content.lookup("#dynamicFieldsContainer");
                if (vbox != null) {
                    HBox hbox = new HBox(label, textField);
                    vbox.getChildren().add(hbox);
                }
            }
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private <Col> StringConverter<Col> getConverterForColumn(TableColumn<T, ?> column) {
        Callback<?, ?> cellFactory = column.getCellFactory();
        if (cellFactory instanceof Callback) {
            Callback<T, TableCell<T, Col>> cellFactoryCallback = (Callback<T, TableCell<T, Col>>) cellFactory;

            // Check if the cellFactoryCallback is null
            if (cellFactoryCallback != null) {
                // Create a temporary TableCell to retrieve the StringConverter
                TableCell<T, Col> tableCell = cellFactoryCallback.call(null);

                if (tableCell != null) {
                    return ((TextFieldTableCell) tableCell).getConverter();
                }
            }
        }

        // If cellFactory or cellFactoryCallback is null, use MyStringConverter directly
        return new MyStringConverter<>((TableColumn<T, Col>) column);
    }

    /**
     * Calculates the total number of pages based on the total item count and items
     * per page.
     *
     * @return The calculated total number of pages.
     */
    private int calculatePageCount() {
        int totalItems = service.count();
        return (totalItems + itemsPerPage - 1) / itemsPerPage;
    }

    /**
     * Creates a Node representing a page with entities for the specified page
     * index.
     *
     * @param pageIndex The index of the page to create.
     * @return The Node representing the specified page.
     */
    private Node createPage(int pageIndex) {
        entities = FXCollections.observableArrayList(
                service.getAllByPage(pageIndex, itemsPerPage));

        tableView.getItems().setAll(entities);
        return tableView;
    }

    /**
     * Initializes the TableController by configuring editable columns, instructing
     * cell population,
     * making columns editable, setting OnEditCommit handlers, and configuring
     * pagination.
     */
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

        entities.addListener((ListChangeListener.Change<? extends T> change) -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    refreshItems();
                }
            }
        });

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
