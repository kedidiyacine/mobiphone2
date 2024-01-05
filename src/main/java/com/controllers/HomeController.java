package com.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.MainApp;
import com.services.AuthService;
import com.utils.Constants;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class HomeController implements Initializable, ControllersWithAuth {
    private AuthService auth;

    @FXML
    private TabPane tabPane;

    @FXML
    private void openClientsTab(ActionEvent event) {
        openTab(Constants.CLIENTS_TAB_TITLE, Constants.CLIENTS_TAB_CONTENT_PATH);
    }

    @FXML
    private void openCommandesTab(ActionEvent event) {
        openTab(Constants.COMMANDES_TAB_TITLE, Constants.COMMANDES_TAB_CONTENT_PATH);
    }

    @FXML
    private void openArticlesTab(ActionEvent event) {
        openTab(Constants.ARTICLES_TAB_TITLE, Constants.ARTICLES_TAB_CONTENT_PATH);
    }

    @FXML
    private void signOut(ActionEvent event) throws IOException {
        auth.signOut();
        MainApp.setRoot(Constants.AUTH_FXML, "authentification");
    }

    private void openTab(String tabText, String contentPath) {
        try {
            // Check if a tab with the given content already exists
            for (Tab existingTab : tabPane.getTabs()) {
                if (existingTab.getContent() != null) {
                    Node existingContent = (Node) existingTab.getContent();
                    if (existingContent.getId() != null && existingContent.getId().equals(contentPath)) {
                        // The tab already exists, don't open a new one
                        tabPane.getSelectionModel().select(existingTab);
                        return;
                    }
                }
            }

            URL resourceUrl = getClass().getResource(contentPath);
            if (resourceUrl == null) {
                System.err.println("FXML file not found: " + contentPath);
                return;
            }

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            Node content = loader.load();

            // Set an ID for the content node based on the contentPath
            content.setId(contentPath);

            Tab newTab = new Tab(tabText);
            newTab.setContent(content);

            tabPane.getTabs().add(newTab);
            tabPane.getSelectionModel().select(newTab);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately based on your application's requirements
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Session useSession = auth.getSession();
        // if (useSession != null) {
        // System.out.println(
        // "Hello Mr." + useSession.getCompte().getLogin() + " our beloved " +
        // useSession.getCompte().getRole());
        // }
    }

    @Override
    public void setAuth(AuthService auth) {
        this.auth = auth;
    }

}
