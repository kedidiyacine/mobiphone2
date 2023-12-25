module com {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires transitive java.sql;
    requires com.zaxxer.hikari;

    // Exported packages
    exports com.services;
    exports com.services.DAOs.session;
    exports com.services.DAOs.compte;
    exports com.services.DAOs.client;

    exports com.models;
    exports com.utils;

    // Open the entire com package to javafx.fxml

    // Open the com.controllers package to javafx.fxml
    opens com.controllers to javafx.fxml;

    // Open the com.services.repertoires.client package for reflection
    opens com.services.DAOs.client;

    // Main package export
    exports com;
}
