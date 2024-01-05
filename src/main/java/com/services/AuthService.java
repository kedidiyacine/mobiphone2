package com.services;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.db.DatabaseUtil;
import com.models.Compte;
import com.models.Session;
import com.services.DAOs.compte.CompteDAO;
import com.services.DAOs.session.SessionDAO;
import com.utils.Constants;
import com.utils.Constants.KEY;

public class AuthService implements AutoCloseable {

    private final CompteDAO compteDAO;
    private final SessionDAO sessionDAO;
    private final Connection connection;
    private Session sessionAutentifie;
    private Compte compteAuthentifie;

    private Map<KEY, String> credentials;

    public AuthService() throws SQLException {
        this.connection = DatabaseUtil.getConnection();
        this.compteDAO = new CompteDAO(connection);
        this.sessionDAO = new SessionDAO(connection);
    }

    public AuthService(Map<KEY, String> credentials) throws SQLException {
        this.connection = DatabaseUtil.getConnection();
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

    public void loadCredentials() {
        Map<KEY, String> credentials = new HashMap<>();
        credentials.put(KEY.LOGIN, compteAuthentifie.getLogin());
        credentials.put(KEY.PASSWORD, compteAuthentifie.getMot_de_passe());
        credentials.put(KEY.ROLE, compteAuthentifie.getRole());

        setCredentials(credentials);
    }

    public Session getSession() {
        if (sessionAutentifie == null) {
            sessionAutentifie = loadSessionData();

            if (sessionAutentifie != null) {
                compteAuthentifie = compteDAO.trouver_par_id(sessionAutentifie.getCompte_id());

                if (compteAuthentifie != null) {
                    loadCredentials();
                    sessionAutentifie = sessionDAO.trouver_par_compte_id(compteAuthentifie.getId());

                    if (isSessionExpired(sessionAutentifie)) {
                        sessionAutentifie = sessionDAO.refreshSession(compteAuthentifie.getId());
                        saveSessionData(sessionAutentifie.getCompte_id(), sessionAutentifie.getDate_debut());
                    }
                }
            }
        }

        return sessionAutentifie;
    }

    public boolean signIn() {
        try {
            compteAuthentifie = verifier();
            if (compteAuthentifie != null) {
                sessionAutentifie = sessionDAO.trouver_par_compte_id(compteAuthentifie.getId());

                if (sessionAutentifie == null) {
                    sessionAutentifie = new Session(compteAuthentifie.getId());
                    sessionDAO.enregistrer(sessionAutentifie);
                    saveSessionData(sessionAutentifie.getCompte_id(), sessionAutentifie.getDate_debut());
                } else if (isSessionExpired(sessionAutentifie)) {
                    // Refresh the session if it's expired
                    sessionAutentifie = sessionDAO.refreshSession(compteAuthentifie.getId());
                    saveSessionData(sessionAutentifie.getCompte_id(),
                            sessionAutentifie.getDate_debut());
                }

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void signOut() {
        try {
            if (sessionAutentifie != null) {
                sessionDAO.supprimer_par_id(sessionAutentifie.getId());
                sessionAutentifie = null;
                compteAuthentifie = null;
                emptySessionData();
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

    // Check if the session has expired
    private boolean isSessionExpired(Session session) {
        if (session == null) {
            return true;
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp sessionStart = session.getDate_debut();

        long elapsedTimeInMillis = now.getTime() - sessionStart.getTime();
        long sessionDurationInMillis = Constants.SESSION_DURATION_MINUTES * 60 * 1000;

        return elapsedTimeInMillis > sessionDurationInMillis;
    }

    // Load session data from file
    private Session loadSessionData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Constants.SESSION_FILE_PATH))) {
            Cipher cipher = Cipher.getInstance(Constants.CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, generateSecretKey(Constants.SECRET_KEY));
            SealedObject sealedObject = (SealedObject) ois.readObject();
            return (Session) sealedObject.getObject(cipher);
        } catch (FileNotFoundException e) {
            // No previous session data
            System.err.println("FileNotFoundException: File not found at path: " + Constants.SESSION_FILE_PATH);
            return null;
        } catch (EOFException e) {
            // End of file reached unexpectedly
            System.err.println("EOFException: End of file reached unexpectedly. Check the file content and structure.");
            // e.printStackTrace();
            return null;
        } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            // Handle other errors
            System.err.println("Error loading session data from file: " + Constants.SESSION_FILE_PATH);
            // e.printStackTrace();
            return null;
        }
    }

    // Save session data to file
    private void saveSessionData(Long accountId, Timestamp date_debut) {
        try {
            // Check if the file exists, and create it if not
            File sessionFile = new File(Constants.SESSION_FILE_PATH);
            if (!sessionFile.exists()) {
                sessionFile.createNewFile();
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(sessionFile))) {
                Cipher cipher = Cipher.getInstance(Constants.CIPHER_ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE, generateSecretKey(Constants.SECRET_KEY));
                SealedObject sealedObject = new SealedObject(new Session(accountId, date_debut), cipher);
                oos.writeObject(sealedObject);
            }
        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | IllegalBlockSizeException e) {
            // Handle errors in saving session data
            e.printStackTrace();
        }
    }

    public void emptySessionData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Constants.SESSION_FILE_PATH))) {
            // Write an empty SealedObject to the file
            Cipher cipher = Cipher.getInstance(Constants.CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, generateSecretKey(Constants.SECRET_KEY));
            SealedObject sealedObject = new SealedObject(null, cipher);
            oos.writeObject(sealedObject);
        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | IllegalBlockSizeException e) {
            // Handle errors in emptying session data
            e.printStackTrace();
        }
    }

    // Generate a secret key for encryption and decryption
    private SecretKey generateSecretKey(String secretKey) throws NoSuchAlgorithmException {
        byte[] keyData = secretKey.getBytes();

        return new SecretKeySpec(keyData, "AES");
    }

    @Override
    public void close() throws Exception {
        if (connection != null && connection.isClosed())
            connection.close();
    }
}
