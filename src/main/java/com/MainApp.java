package com;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Connection;

import com.controllers.ControllersWithAuth;
import com.db.DatabaseUtil;
import com.services.authentification.AuthService;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainApp extends Application {
    private static Stage stage;
    private static AuthService auth;

    @Override
    public void start(@SuppressWarnings("exports") Stage s) throws IOException {
        stage = s;
        try {
            Connection connection = DatabaseUtil.getConnection();
            auth = new AuthService(connection);
            setRoot("auth"); // "auth"
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void setRoot(String fxml) throws IOException {
        setRoot(fxml, stage.getTitle());
    }

    public static void setRoot(String fxml, String title) throws IOException {
        stage.setScene(new Scene(loadFXML(fxml), 1024, 960, Color.BLACK));
        stage.setTitle(title);
        stage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/fxml/" + fxml + ".fxml"));

        fxmlLoader.setControllerFactory(param -> {
            Object controller = null;
            try {
                controller = param.getDeclaredConstructor().newInstance();
                if (controller instanceof ControllersWithAuth) {
                    ((ControllersWithAuth) controller).setAuth(auth);
                }
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }

            return controller;
        });
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
