package com.github.lioia;

import com.github.lioia.persistence.FilePersistence;
import com.github.lioia.ui.ListPage;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // TODO: create persistence layer
        //       - if there is no credenziali_database.properties and no schema_database.sql; use FilePersistence
        //       - otherwise use DatabasePersistence
        // Let Swing decide when to run the UI
        SwingUtilities.invokeLater(new ListPage(new FilePersistence()));
    }
}