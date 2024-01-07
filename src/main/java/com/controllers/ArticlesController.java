package com.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

public class ArticlesController implements Initializable {
    @FXML
    private VBox root;

    @FXML
    private ComboBox<String> cmbArticleType;

    private final TelephoneMobileController phoneCtrl = new TelephoneMobileController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cmbArticleType.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {

                    if ("telephone mobile".equals(newSelection)) {
                        reloadFXML(newSelection, phoneCtrl);
                    }
                    if ("ligne telephone".equals(newSelection)) {
                        reloadFXML(newSelection, null);
                    }
                });

        cmbArticleType.getSelectionModel().selectFirst();

    }

    private void reloadFXML(String selected, Object controller) {
        try {
            // Remove the last child (if exists)
            int lastIndex = root.getChildren().size() - 1;

            if (lastIndex >= 1) {
                root.getChildren().remove(lastIndex);
            }
            if ("telephone mobile".equals(selected)) {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/fxml/agent-commercial/" +
                                selected.replace(" ", "_") + ".fxml"));
                loader.setController(controller);

                // if (phoneCtrl.getTableCtrl() == null) {
                VBox loadedContent = loader.load();

                // Add the new content to the VBox
                root.getChildren().add(loadedContent);
                // }
            }
            // Add any additional setup for the loaded FXML

        } catch (IOException e) {
            e.printStackTrace();
            root.getChildren().clear();
        }
    }

}
