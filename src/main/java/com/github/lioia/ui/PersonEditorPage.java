package com.github.lioia.ui;

import com.github.lioia.models.Person;
import com.github.lioia.persistence.PersistenceLayer;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

public class PersonEditorPage implements Runnable {
    private final JDialog dialog;
    private final Person person;
    private final PersistenceLayer persistence;

    private final JLabel errorLabel;

    private JTextField nameField;
    private JTextField surnameField;
    private JTextField addressField;
    private JTextField phoneField;
    private JFormattedTextField ageField;

    public PersonEditorPage(JFrame parent, Person person, PersistenceLayer persistence) {
        this.person = person;
        this.persistence = persistence;

        // Create new Modal dialog
        dialog = new JDialog(parent, "Editor Persona", true);
        // Set layout a Border
        dialog.setLayout(new BorderLayout());
        // Position is relative to the parent window
        dialog.setLocationRelativeTo(parent);
        dialog.setSize(400, 300);

        // Centered Header
        JLabel header = new JLabel("Editor Persona", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18));

        // Footer
        JPanel footer = new JPanel(new BorderLayout());
        JPanel buttonsRow = createButtonsRow();
        errorLabel = new JLabel("", SwingConstants.CENTER);
        footer.add(errorLabel, BorderLayout.NORTH);
        footer.add(buttonsRow, BorderLayout.SOUTH);

        // Create UI
        dialog.add(header, BorderLayout.NORTH);
        dialog.add(createForm(), BorderLayout.CENTER);
        dialog.add(footer, BorderLayout.SOUTH);
    }

    @Override
    public void run() {
        // Build UI
        dialog.pack();
        // Show UI
        dialog.setVisible(true);
    }

    private JPanel createForm() {
        // Create new Form panel
        JPanel form = new JPanel();
        // Using a GroupLayout to automatically adjusts the spacing between the elements
        GroupLayout layout = new GroupLayout(form);
        form.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // Create labels
        JLabel nameLabel = new JLabel("Name:");
        JLabel surnameLabel = new JLabel("Surname:");
        JLabel addressLabel = new JLabel("Address:");
        JLabel phoneLabel = new JLabel("Phone:");
        JLabel ageLabel = new JLabel("Age:");
        // Create fields
        nameField = new JTextField(10);
        nameField.setText(person == null ? "" : person.getName());
        surnameField = new JTextField(10);
        surnameField.setText(person == null ? "" : person.getSurname());
        addressField = new JTextField(10);
        addressField.setText(person == null ? "" : person.getAddress());
        phoneField = new JTextField(10);
        phoneField.setText(person == null ? "" : person.getPhone());

        // Using a number formatter to only allow numbers in the text field
        NumberFormatter formatter = new NumberFormatter(NumberFormat.getNumberInstance());
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        ageField = new JFormattedTextField(formatter);
        ageField.setColumns(10);
        ageField.setValue(person == null ? 0 : person.getAge());

        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(nameLabel)
                .addComponent(surnameLabel)
                .addComponent(addressLabel)
                .addComponent(phoneLabel)
                .addComponent(ageLabel));
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(nameField)
                .addComponent(surnameField)
                .addComponent(addressField)
                .addComponent(phoneField)
                .addComponent(ageField));
        layout.setHorizontalGroup(hGroup);

        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(nameLabel)
                .addComponent(nameField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(surnameLabel)
                .addComponent(surnameField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(addressLabel)
                .addComponent(addressField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(phoneLabel)
                .addComponent(phoneField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(ageLabel)
                .addComponent(ageField));
        layout.setVerticalGroup(vGroup);

        return form;
    }

    private JPanel createButtonsRow() {
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Save button
        JButton save = new JButton("Salva");
        save.addActionListener(event -> {
            // Get data from fields
            String name = nameField.getText();
            String surname = surnameField.getText();
            String address = addressField.getText();
            String phone = phoneField.getText();
            int age = Integer.parseInt(ageField.getText());

            // Check correctness of values
            if (name.isEmpty() || surname.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                errorLabel.setText("È presente un errore nei dati inseriti");
                return;
            }

            if (person == null) {
                // No person was passed; this is a new person to create
                try {
                    persistence.addPerson(name, surname, address, phone, age);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                // There was a previous instance of person; this is an edit
                try {
                    persistence.editPerson(person.getId(), name, surname, address, phone, age);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            dialog.dispose();
        });

        // Close modal dialog
        JButton cancel = new JButton("Annulla");
        cancel.addActionListener(e -> dialog.dispose());

        // Add buttons to the row
        buttons.add(cancel);
        buttons.add(save);

        return buttons;
    }
}