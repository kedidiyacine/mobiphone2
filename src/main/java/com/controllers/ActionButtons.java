package com.controllers;

import javafx.scene.control.Button;

public class ActionButtons {
    private Button saveButton;
    private Button deleteButton;
    private Button refreshButton;

    public ActionButtons(Button saveButton, Button deleteButton, Button refreshButton) {
        this.saveButton = saveButton;
        this.deleteButton = deleteButton;
        this.refreshButton = refreshButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }
}
