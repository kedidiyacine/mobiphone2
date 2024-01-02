package com.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Constants {

    private Constants() {
    }

    public enum KEY {
        LOGIN, PASSWORD, ROLE
    }

    // Add any other constants here

    public static final String AUTH_FXML = "auth";
    public static final int MIN_WIDTH = 960;
    public static final int MIN_HEIGHT = 600;

    // Naming convention for constants: UPPERCASE_WITH_UNDERSCORES
    public static final String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String CONFIRMATION_MESSAGE = "Are you sure you want to confirm these changes?";
    public static final String DELETION_MESSAGE = "Are you sure you want to delete this item?";
    public static final String REFRESH_TOOLTIP_MSG = "You can refresh once again after 5 seconds";
    public static final int THROTTLE_DURATION = 5;

    public static final int DEFAULT_INITIAL_PAGE = 1;
    public static final int DEFAULT_ITEMS_PER_PAGE = 10;

    public static final Set<String> NON_EDITABLE_COLUMNS = new HashSet<>(
            Arrays.asList("id", "date_creation", "date_maj"));

    public static final String ARTICLE_TABLE_NAME = "article";
    public static final String TELEPHONE_TABLE_NAME = "telephone_mobile";
    public static final String TELEPHONE_MOBILE_TYPE = "telephone mobile";
    public static final String INSERT_ARTICLE_SQL = "INSERT INTO article (type, libelle, prix_vente, qt_stock) VALUES (?, ?, ?, ?)";
    public static final String INSERT_TYPE_SQL = "INSERT INTO %s (id_article, reference, marque, modele) VALUES (?, ?, ?, ?)";

    public static final String SELECT_ARTICLE_BY_ID_SQL = "SELECT * FROM %s INNER JOIN %s ON %s.id = %s.id_article WHERE %s.id = ?";
    public static final String SELECT_ARTICLES_BY_PAGE_SQL = "SELECT * FROM %s INNER JOIN %s ON %s.id = %s.id_article ORDER BY %s.id LIMIT ? OFFSET ?";

    public static final List<String> ARTICLE_COLUMNS = Arrays.asList("id", "type", "libelle", "prix_vente", "qt_stock",
            "date_creation", "date_maj");

    public static final List<String> TELEPHONE_COLUMNS = Arrays.asList("id_article", "reference", "modele", "marque");

}
