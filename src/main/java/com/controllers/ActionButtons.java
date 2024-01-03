package com.controllers;

import javafx.scene.control.Button;

public class ActionButtons {
    private Button saveButton;
    private Button createButton;
    private Button deleteButton;
    private Button refreshButton;

    public ActionButtons(Button saveButton, Button deleteButton, Button refreshButton, Button createButton) {
        this.saveButton = saveButton;
        this.deleteButton = deleteButton;
        this.refreshButton = refreshButton;
        this.createButton = createButton;
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

    public Button getCreateButton() {
        return createButton;
    }

}
