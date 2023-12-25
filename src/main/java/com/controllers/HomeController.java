package com.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.models.Session;
import com.services.AuthService;

import javafx.fxml.Initializable;

public class HomeController implements Initializable, ControllersWithAuth {
    private AuthService auth;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Session useSession = auth.getSession();
        if (useSession != null) {
            System.out.println(
                    "Hello Mr." + useSession.getCompte().getLogin() + " our beloved " +
                            useSession.getCompte().getRole());
        }
    }

    @Override
    public void setAuth(AuthService auth) {
        this.auth = auth;
    }

}
