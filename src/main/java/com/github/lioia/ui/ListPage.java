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

        // Header
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        toolBar.add(createAddButton());
        toolBar.add(createEditButton());
        toolBar.add(createRemoveButton());

        // Table
        table = new JTable(new PersonTableModel(persistence.getAll()));
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
            if (table.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(frame, "È necessario selezionare una persona");
                return;
            }
            Person selected = persistence.getAll().get(table.getSelectedRow());
            new PersonEditorPage(frame, selected, persistence).run();
            ((PersonTableModel) table.getModel()).fireTableDataChanged();
        });
        return button;
    }

    private JButton createRemoveButton() {
        FontIcon icon = FontIcon.of(FontAwesomeSolid.TRASH, 18, Color.RED);
        JButton button = new JButton("Elimina", icon);
        button.addActionListener(_ -> {
            if (table.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(frame, "È necessario selezionare una persona");
                return;
            }
            Person selected = persistence.getAll().get(table.getSelectedRow());
            int confirmDialog = JOptionPane.showConfirmDialog(frame, "Eliminare la persona " + selected.getName() + " " + selected.getSurname() + "?");
            if (confirmDialog == JOptionPane.YES_OPTION) {
                persistence.delete(selected);
                ((PersonTableModel) table.getModel()).fireTableDataChanged();
            }
        });
        return button;
    }
}