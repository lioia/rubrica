package com.github.lioia;

import com.github.lioia.persistence.DatabasePersistence;
import com.github.lioia.persistence.FilePersistence;
import com.github.lioia.persistence.PersistenceLayer;
import com.github.lioia.ui.LoginPage;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        // Create persistence layer
        PersistenceLayer persistenceLayer;
        if (Files.exists(Paths.get("credenziali_database.properties"))) {
            // Using db as persistence
            try {
                persistenceLayer = new DatabasePersistence();
            } catch (IOException e) {
                // Failure when reading credentials
                JOptionPane.showMessageDialog(
                        null,
                        e.getMessage(),
                        "Errore nella lettura delle credenziali",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            } catch (SQLException e) {
                // Failure when connecting to db
                JOptionPane.showMessageDialog(
                        null,
                        e.getMessage(),
                        "Errore nella connessione al database",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        } else {
            // There is no credentials for the database; using file-system as persistence (only if informazioni.txt exists)
            persistenceLayer = new FilePersistence();
        }

        // Let Swing decide when to run the UI
        SwingUtilities.invokeLater(new LoginPage(persistenceLayer));
    }
}