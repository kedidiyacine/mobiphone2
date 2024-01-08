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

        // UI
        public static final String AUTH_FXML = "auth";
        public static final String AGENT_HOME = "home";
        public static final int MIN_WIDTH = 960;
        public static final int MIN_HEIGHT = 600;

        public static List<String> ROLES = Arrays.asList("agent commercial", "responsable Commercial", "comptable");
        public static List<String> PATHS = Arrays.asList("home", "home", "home");

        public static final String CREATION_MODAL_CONTENT_PATH = "/fxml/agent-commercial/create.fxml";

        // Session
        public static final String SESSION_FILE_PATH = ".session.ser";
        // eventually will be moved to a config file
        public static final String SECRET_KEY = "23A59E7BFB78CA40E1155839E3800930";
        public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
        public static final int SESSION_DURATION_MINUTES = 60 * 24 * 365; // Adjust as needed

        // Tabs
        public static final String CLIENTS_TAB_TITLE = "Clients";
        public static final String CLIENTS_TAB_CONTENT_PATH = "/fxml/agent-commercial/clients.fxml";

        public static final String COMMANDES_TAB_TITLE = "Commandes";
        public static final String COMMANDES_TAB_CONTENT_PATH = "/fxml/agent-commercial/commandes.fxml";

        public static final String ARTICLES_TAB_TITLE = "Articles";
        public static final String ARTICLES_TAB_CONTENT_PATH = "/fxml/agent-commercial/articles.fxml";

        //
        public static final String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
        public static final String CONFIRMATION_MESSAGE = "Are you sure you want to confirm these changes?";
        public static final String DELETION_MESSAGE_CONTENT = "Once you confirm, these items will be permanantely deleted!";
        public static final String DELETION_MESSAGE_HEADER = "Are you sure you want to delete all %d item(s)?";

        public static final String REFRESH_TOOLTIP_MSG = "You can refresh once again after 5 seconds";
        public static final int THROTTLE_DURATION = 5;

        // PAGES
        public static final int DEFAULT_INITIAL_PAGE = 1;
        public static final int DEFAULT_ITEMS_PER_PAGE = 10;

        // SQL
        public static final Set<String> NON_EDITABLE_COLUMNS = new HashSet<>(
                        Arrays.asList("id", "date_creation", "date_maj"));

        // article
        public static final String ARTICLE_TABLE_NAME = "article";
        public static final List<String> ARTICLE_COLUMNS = Arrays.asList("id", "type", "libelle", "prix_vente",
                        "qt_stock",
                        "date_creation", "date_maj");
        public static final String ARTICLE_PRIMARY_KEY = "id";
        public static final String SUB_ARTICLE_PRIMARY_KEY = "id_article";

        public static final String INSERT_ARTICLE_SQL = "INSERT INTO article (type, libelle, prix_vente, qt_stock) VALUES (?, ?, ?, ?)";
        public static final String SELECT_ARTICLE_BY_ID_SQL = "SELECT * FROM %s INNER JOIN %s ON %s.id = %s.id_article WHERE %s.id = ?";
        public static final String SELECT_ARTICLES_BY_PAGE_SQL = "SELECT * FROM %s INNER JOIN %s ON %s.id = %s.id_article ORDER BY %s.date_maj DESC LIMIT ? OFFSET ?";
        public static final String SELECT_ARTICLES = "SELECT * FROM %s INNER JOIN %s ON %s.id = %s.id_article ORDER BY %s.date_maj DESC";

        // telephone_mobile
        public static final String TELEPHONE_TABLE_NAME = "telephone_mobile";
        public static final String TELEPHONE_MOBILE_TYPE = "telephone mobile";
        public static final List<String> TELEPHONE_COLUMNS = Arrays.asList("id_article", "reference", "modele",
                        "marque");

        public static final String INSERT_TYPE_SQL = "INSERT INTO %s (id_article, reference, marque, modele) VALUES (?, ?, ?, ?)";

        // ligne_telephonique
        public static final String LIGNE_TELEPHONIQUE_TABLE_NAME = "ligne_telephonique";
        public static final String LIGNE_TELEPHONIQUE_TYPE = "ligne telephonique";
        public static final List<String> LIGNE_TELEPHONIQUE_COLUMNS = Arrays.asList("id_article", "numero", "operateur",
                        "montant_min_consommation");
}
