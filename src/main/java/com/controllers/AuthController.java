package com.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.MainApp;
import com.services.AuthService;
import com.utils.Constants;
import com.utils.Constants.KEY;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AuthController implements Initializable, ControllersWithAuth {
    private AuthService auth;
    private String selectedRole;

    @FXML
    private TextField login;
    @FXML
    private TextField pwd;
    @FXML
    private ComboBox<String> role_comboBox;
    @FXML
    private Button btn_login;

    @FXML
    private void handleCbxRole(ActionEvent event) {
        selectedRole = role_comboBox.getValue();
    }

    private void handleBtnLogin(ActionEvent event) {
        try {
            Map<KEY, String> credentialsMap = new HashMap<>();
            credentialsMap.put(KEY.LOGIN, login.getText());
            credentialsMap.put(KEY.PASSWORD, pwd.getText());
            credentialsMap.put(KEY.ROLE, selectedRole.toLowerCase());

            auth.setCredentials(credentialsMap);
            auth.signIn();

            if (auth.getSession() != null) {
                String path = Constants.PATHS.get(Constants.ROLES.indexOf(selectedRole));

                // Here we can direct the loggedIn user to the interface specific for his role.
                MainApp.setRoot(path, com.utils.StringUtils.capitalizeWord(path));

            } else {
                // did not login successfully
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setAuth(AuthService auth) {
        this.auth = auth;
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        role_comboBox.getItems().addAll(Constants.ROLES);

        // Set an initial selected item if needed
        role_comboBox.getSelectionModel().selectFirst();
        selectedRole = role_comboBox.getValue();

        btn_login.setOnAction((e) -> {
            handleBtnLogin(e);
        });

    }
}
