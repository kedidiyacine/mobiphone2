package com.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.utils.Constants;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

public class ArticlesController implements Initializable {
    @FXML
    private VBox root;

    @FXML
    private ComboBox<String> cmbArticleType;

    private final TelephoneMobileController phoneCtrl = new TelephoneMobileController();
    private final LigneTelephoniqueController ligneCtrl = new LigneTelephoniqueController();
    private final CarteTelephoniqueController carteCtrl = new CarteTelephoniqueController();
    private final Cle3gController cle3gCtrl = new Cle3gController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cmbArticleType.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {

                    if (newSelection.equals(Constants.TELEPHONE_MOBILE_TYPE)) {
                        reloadFXML(newSelection, phoneCtrl);
                    }
                    if (newSelection.equals(Constants.LIGNE_TELEPHONIQUE_TYPE)) {
                        reloadFXML(newSelection, ligneCtrl);
                    }

                    if (newSelection.equals(Constants.CARTE_TELEPHONIQUE_TYPE)) {
                        reloadFXML(newSelection, carteCtrl);
                    }

                    if (newSelection.equals(Constants.CLE_3G_TYPE)) {
                        reloadFXML(newSelection, cle3gCtrl);
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

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/agent-commercial/" +
                            selected.replace(" ", "_") + ".fxml"));
            loader.setController(controller);

            VBox loadedContent = loader.load();

            // Add the new content to the VBox
            root.getChildren().add(loadedContent);

            // Add any additional setup for the loaded FXML

        } catch (IOException e) {
            e.printStackTrace();
            root.getChildren().clear();
        }
    }

}
