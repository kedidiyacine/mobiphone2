package com;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

import com.controllers.ControllersWithAuth;
import com.db.DatabaseUtil;
import com.services.AuthService;
import com.utils.Constants;

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
            setRoot(Constants.AUTH_FXML);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void setRoot(String fxml) throws IOException {
        setRoot(fxml, stage.getTitle());
    }

    public static void setRoot(String fxml, String title) throws IOException {
        stage.setScene(new Scene(loadFXML(fxml), Color.BLACK));
        stage.setTitle(title);
        stage.setWidth(Constants.MIN_WIDTH);
        stage.setHeight(Constants.MIN_HEIGHT);

        stage.setMinWidth(Constants.MIN_WIDTH);
        stage.setMinHeight(Constants.MIN_HEIGHT);

        stage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/fxml/" + fxml + ".fxml"));

        fxmlLoader.setControllerFactory(param -> {
            try {
                Object controller = param.getDeclaredConstructor().newInstance();
                if (controller instanceof ControllersWithAuth) {
                    ((ControllersWithAuth) controller).setAuth(auth);
                }
                return controller;
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
                throw new RuntimeException("Error creating controller", e);
            }
        });

        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
