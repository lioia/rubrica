package com.github.lioia.persistence;

import com.github.lioia.models.Person;
import com.github.lioia.models.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabasePersistence implements PersistenceLayer {
    // Keep a single long-lived connection
    private final Connection connection;

    public DatabasePersistence() throws IOException, SQLException {
        // Load data from properties file
        Properties properties = new Properties();
        properties.load(new FileInputStream("credenziali_database.properties"));

        // Read parameters from properties file
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");
        String ip = properties.getProperty("db.ip");
        String port = properties.getProperty("db.port");
        String dbName = properties.getProperty("db.name");
        String dbType = properties.getProperty("db.type");

        // Create connection URI from parameters
        String connectionUrl = "jdbc:" + dbType + "://" + ip + ":" + port + "/" + dbName;
        // Connect to db
        connection = DriverManager.getConnection(connectionUrl, username, password);
    }

    @Override
    public List<Person> getAllPersons() throws Exception {
        List<Person> persons = new ArrayList<>();
        // Select all persons
        String query = "SELECT * FROM person";
        // Create statement
        Statement statement = connection.createStatement();
        // Run query
        ResultSet set = statement.executeQuery(query);
        // Loop through all the persons
        while (set.next()) {
            // Get data from db
            int id = set.getInt("id");
            String name = set.getString("name");
            String surname = set.getString("surname");
            String address = set.getString("address");
            String phone = set.getString("phone");
            int age = set.getInt("age");
            // Add person to the list
            persons.add(new Person(id, name, surname, address, phone, age));
        }

        return persons;
    }

    @Override
    public void addPerson(String name, String surname, String address, String phone, int age) throws Exception {
        // Insert single person
        String query = "INSERT INTO person (name, surname, address, phone, age) VALUES (?, ?, ?, ?, ?)";
        // Create statement to fill the data
        try (PreparedStatement statement = createStatement(query, name, surname, address, phone, age)) {
            // Run query
            statement.executeUpdate();
        }
    }

    @Override
    public void editPerson(int id, String name, String surname, String address, String phone, int age) throws Exception {
        // Update person based on id
        String query = "UPDATE person SET name = ?, surname = ?, address = ?, phone = ?, age = ? WHERE id = ?";
        // Create statement to fill the data
        try (PreparedStatement statement = createStatement(query, name, surname, address, phone, age)) {
            // Add id to the statement data
            statement.setInt(6, id);
            // Run query
            statement.executeUpdate();
        }
    }

    @Override
    public void deletePerson(Person person) throws Exception {
        // Delete person from id
        String query = "DELETE FROM person WHERE id = ?";
        // Create statement
        PreparedStatement statement = connection.prepareStatement(query);
        // Pass id as parameter
        statement.setInt(1, person.getId());
        // Run query
        statement.executeUpdate();
    }

    @Override
    public boolean isUserValid(User user) {
        // Get user based on username and password
        String query = "SELECT * FROM user WHERE username = ? AND password = ?";
        try {
            // Create statement
            PreparedStatement statement = connection.prepareStatement(query);
            // Pass arguments to statement
            statement.setString(1, user.username());
            statement.setString(2, user.password());

            // Run query
            ResultSet set = statement.executeQuery();
            // User was found
            return set.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public void save() {
        // Saving in this context means closing the db connection
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Utility method to create a common statement; used by `addPerson` and `editPerson`
    private PreparedStatement createStatement(String query, String name, String surname, String address, String phone, int age) throws SQLException {
        // Create statement
        PreparedStatement statement = connection.prepareStatement(query);
        // Pass common arguments to statement
        statement.setString(1, name);
        statement.setString(2, surname);
        statement.setString(3, address);
        statement.setString(4, phone);
        statement.setInt(5, age);
        return statement;
    }
}