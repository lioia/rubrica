package com.github.lioia.ui;

import com.github.lioia.models.Person;
import com.github.lioia.persistence.PersistenceLayer;
import com.github.lioia.utils.PersonTableModel;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ListPage implements Runnable {
    private final JFrame frame;
    private final PersistenceLayer persistence;
    private final JTable table;

    public ListPage(PersistenceLayer persistence) {
        this.persistence = persistence;

        // Create new JFrame
        frame = new JFrame("Rubrica");
        // Close JFrame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Center frame
        frame.setLocationRelativeTo(null);
        frame.setSize(800, 600);

        // Header
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.add(createAddButton());
        toolBar.add(createEditButton());
        toolBar.add(createRemoveButton());

        // Table
        table = new JTable(new PersonTableModel(persistence));
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);

        // Add UI to frame
        frame.add(toolBar, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Before closing, save data
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                persistence.save();
                super.windowClosing(e);
            }
        });
    }

    @Override
    public void run() {
        // Build UI
        frame.pack();
        // Set frame as visible (by default the frame is not visible)
        frame.setVisible(true);
    }

    private JButton createAddButton() {
        // Create button with icon
        FontIcon icon = FontIcon.of(FontAwesomeSolid.PLUS, 18, Color.GREEN);
        JButton button = new JButton("Aggiungi", icon);
        button.addActionListener(e -> {
            // Open Person Editor
            new PersonEditorPage(frame, null, persistence).run();
            // Update data
            ((PersonTableModel) table.getModel()).fireTableDataChanged();
        });

        return button;
    }

    private JButton createEditButton() {
        // Create button with icon
        FontIcon icon = FontIcon.of(FontAwesomeSolid.EDIT, 18, Color.ORANGE);
        JButton button = new JButton("Modifica", icon);
        button.addActionListener(e -> {
            // Get selected person
            Person selected = selectPerson();
            if (selected == null) {
                // No person were selected
                JOptionPane.showMessageDialog(frame, "È necessario selezionare una persona");
                return;
            }
            // Open Person Editor with the selected person
            new PersonEditorPage(frame, selected, persistence).run();
            // Update table data
            ((PersonTableModel) table.getModel()).fireTableDataChanged();
        });
        return button;
    }

    private JButton createRemoveButton() {
        // Create button with icon
        FontIcon icon = FontIcon.of(FontAwesomeSolid.TRASH, 18, Color.RED);
        JButton button = new JButton("Elimina", icon);
        button.addActionListener(event -> {
            // Select a person
            Person selected = selectPerson();
            if (selected == null) {
                // No person were selected
                JOptionPane.showMessageDialog(frame, "È necessario selezionare una persona");
                return;
            }
            // Show delete message dialog
            int confirmDialog = JOptionPane.showConfirmDialog(frame, "Eliminare la persona " + selected.getName() + " " + selected.getSurname() + "?");
            if (confirmDialog == JOptionPane.YES_OPTION) {
                // User wants to delete the person
                try {
                    persistence.deletePerson(selected);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Update table data
                ((PersonTableModel) table.getModel()).fireTableDataChanged();
            }
        });
        return button;
    }

    private Person selectPerson() {
        try {
            // Check if a person was selected
            if (table.getSelectedRow() == -1) {
                return null;
            }
            // Return the selected person
            return persistence.getAllPersons().get(table.getSelectedRow());
        } catch (Exception e) {
            return null;
        }
    }
}