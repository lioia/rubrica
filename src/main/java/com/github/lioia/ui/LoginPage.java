package com.github.lioia.ui;

import com.github.lioia.models.User;
import com.github.lioia.persistence.PersistenceLayer;

import javax.swing.*;
import java.awt.*;

public class LoginPage implements Runnable {
    private final JFrame frame;

    public LoginPage(PersistenceLayer persistence) {
        // Create new JFrame
        frame = new JFrame("Rubrica");
        // Close JFrame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Center frame
        frame.setLocationRelativeTo(null);
        frame.setSize(400, 300);

        JPanel form = new JPanel();
        GroupLayout layout = new GroupLayout(form);
        form.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel usernameLabel = new JLabel("Username");
        JLabel passwordLabel = new JLabel("Password");
        JLabel errorLabel = new JLabel("");

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton login = new JButton("Login");
        login.addActionListener(_ -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty()|| password.isEmpty()) {
                errorLabel.setText("Username e password sono richiesti");
                return;
            }

            if (!persistence.isUserValid(new User(username, password))) {
                errorLabel.setText("Username e password non validi");
                return;
            }

            frame.dispose();
            new ListPage(persistence).run();
        });

        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(usernameLabel)
                .addComponent(passwordLabel));
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(usernameField)
                .addComponent(passwordField));
        layout.setHorizontalGroup(hGroup);

        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(usernameLabel)
                .addComponent(usernameField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(passwordLabel)
                .addComponent(passwordField));
        layout.setVerticalGroup(vGroup);

        // Header
        JLabel header = new JLabel("Login", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18));

        // Footer
        JPanel footer = new JPanel(new BorderLayout());
        footer.add(errorLabel, BorderLayout.NORTH);
        footer.add(login, BorderLayout.SOUTH);

        frame.add(header, BorderLayout.NORTH);
        frame.add(form, BorderLayout.CENTER);
        frame.add(footer, BorderLayout.SOUTH);
    }

    @Override
    public void run() {
        frame.pack();
        frame.setVisible(true);
    }
}