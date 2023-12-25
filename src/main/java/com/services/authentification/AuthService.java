package com.services.authentification;

import java.sql.Connection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import com.utils.Constants.KEY;

import com.models.Compte;
import com.models.Session;
import com.services.DAOs.compte.CompteDAO;
import com.services.DAOs.session.SessionDAO;

public class AuthService {

    private final CompteDAO compteDAO;
    private final SessionDAO sessionDAO;
    private Session sessionAutentifie;
    private Compte compteAuthentifie;

    private Map<KEY, String> credentials;

    public AuthService(Connection connection) {
        this.compteDAO = new CompteDAO(connection);
        this.sessionDAO = new SessionDAO(connection);
    }

    public AuthService(Connection connection, Map<KEY, String> credentials) {
        this.compteDAO = new CompteDAO(connection);
        this.sessionDAO = new SessionDAO(connection);
        this.credentials = credentials;
    }

    public Compte verifier() {
        Compte compte = compteDAO.trouver_par_login(credentials.get(KEY.LOGIN));
        // verifier si le compte existe et le mot de passe et le role sont les mémes
        if (compte != null && compte.getMot_de_passe().equals(credentials.get(KEY.PASSWORD))
                && compte.getRole().equals(credentials.get(KEY.ROLE))) {
            return compte;
        }
        return null;
    }

    public Session getSession() {
        return sessionAutentifie;
    }

    public void signIn() {
        try {
            compteAuthentifie = verifier();
            if (compteAuthentifie != null) {
                sessionAutentifie = new Session(compteAuthentifie);
                sessionDAO.enregistrer(sessionAutentifie);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void signOut() {
        try {
            if (sessionAutentifie != null) {
                sessionDAO.supprimer_par_id(sessionAutentifie.getId());
                sessionAutentifie = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<KEY, String> getCredentials() {
        Map<KEY, String> c = new HashMap<>(credentials);
        c.remove(KEY.PASSWORD);
        return Collections.unmodifiableMap(c);
    }

    public void setCredentials(Map<KEY, String> credentials) {
        this.credentials = credentials;
    }

}
