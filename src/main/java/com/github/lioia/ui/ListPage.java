package com.github.lioia.ui;

import com.github.lioia.models.Person;
import com.github.lioia.persistence.PersistenceLayer;
import com.github.lioia.utils.PersonTableModel;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.util.List;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ListPage implements Runnable {
    private final JFrame frame;
    private final PersistenceLayer persistence;
    private JTable table;

    public ListPage(PersistenceLayer persistence) {
        this.persistence = persistence;

        // Create new JFrame
        frame = new JFrame("Rubrica");
        // Close JFrame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Center frame
        frame.setLocationRelativeTo(null);

        // Header
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        toolBar.add(createAddButton());
        toolBar.add(createEditButton());
        toolBar.add(createRemoveButton());

        List<Person> persons;
        // Table
        try {
            persons = persistence.getAll();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            frame.dispose();
            return;
        }
        table = new JTable(new PersonTableModel(persistence));
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);

        frame.add(toolBar, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

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
        FontIcon icon = FontIcon.of(FontAwesomeSolid.PLUS, 18, Color.GREEN);
        JButton button = new JButton("Aggiungi", icon);
        button.addActionListener(_ -> {
            new PersonEditorPage(frame, null, persistence).run();
            ((PersonTableModel) table.getModel()).fireTableDataChanged();
        });

        return button;
    }

    private JButton createEditButton() {
        FontIcon icon = FontIcon.of(FontAwesomeSolid.EDIT, 18, Color.ORANGE);
        JButton button = new JButton("Modifica", icon);
        button.addActionListener(_ -> {
            Person selected = selectPerson();
            if (selected == null) {
                JOptionPane.showMessageDialog(frame, "È necessario selezionare una persona");
                return;
            }
            new PersonEditorPage(frame, selected, persistence).run();
            ((PersonTableModel) table.getModel()).fireTableDataChanged();
        });
        return button;
    }

    private JButton createRemoveButton() {
        FontIcon icon = FontIcon.of(FontAwesomeSolid.TRASH, 18, Color.RED);
        JButton button = new JButton("Elimina", icon);
        button.addActionListener(_ -> {
            Person selected = selectPerson();
            if (selected == null) {
                JOptionPane.showMessageDialog(frame, "È necessario selezionare una persona");
                return;
            }
            int confirmDialog = JOptionPane.showConfirmDialog(frame, "Eliminare la persona " + selected.getName() + " " + selected.getSurname() + "?");
            if (confirmDialog == JOptionPane.YES_OPTION) {
                try {
                    persistence.delete(selected);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                ((PersonTableModel) table.getModel()).fireTableDataChanged();
            }
        });
        return button;
    }

    private Person selectPerson() {
        try {
            if (table.getSelectedRow() == -1) {
                return null;
            }
            return persistence.getAll().get(table.getSelectedRow());
        } catch (Exception e) {
            return null;
        }
    }
}