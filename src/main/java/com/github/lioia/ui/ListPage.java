package com.github.lioia.ui;

import com.github.lioia.models.Person;
import com.github.lioia.persistence.PersistenceLayer;
import com.github.lioia.utils.PersonTableModel;

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

        // Table
        table = new JTable(new PersonTableModel(persistence.getAll()));
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Row of buttons
        JPanel buttonsRow = createButtons();
        frame.add(buttonsRow, BorderLayout.SOUTH);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                persistence.save();
                super.windowClosing(e);
            }
        });
    }

    private JPanel createButtons() {
        JPanel buttonsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        // Add button
        JButton addButton = new JButton("Aggiungi");
        addButton.addActionListener(_ -> {
            new PersonEditorPage(frame, null, persistence).run();
            ((PersonTableModel) table.getModel()).fireTableDataChanged();
        });

        // Remove button
        JButton removeButton = new JButton("Elimina");
        removeButton.addActionListener(_ -> {
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
        // Edit button
        JButton editButton = new JButton("Modifica");
        editButton.addActionListener(_ -> {
            if (table.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(frame, "È necessario selezionare una persona");
                return;
            }
            Person selected = persistence.getAll().get(table.getSelectedRow());
            new PersonEditorPage(frame, selected, persistence).run();
            ((PersonTableModel) table.getModel()).fireTableDataChanged();
        });
        buttonsRow.add(removeButton);
        buttonsRow.add(editButton);
        buttonsRow.add(addButton);
        return buttonsRow;
    }

    @Override
    public void run() {
        // Build UI
        frame.pack();
        // Set frame as visible (by default the frame is not visible)
        frame.setVisible(true);
    }
}