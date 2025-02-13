package com.github.lioia.persistence;

import com.github.lioia.models.Person;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabasePersistence implements PersistenceLayer {
    private final Connection connection;
    private List<Person> persons;

    public DatabasePersistence() throws IOException, SQLException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("credenziali_database.properties"));

        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");
        String ip = properties.getProperty("db.ip");
        String port = properties.getProperty("db.port");
        String dbName = properties.getProperty("db.name");
        String dbType = properties.getProperty("db.type");

        String connectionUrl = "jdbc:" + dbType + "://" + ip + ":" + port + "/" + dbName;
        connection = DriverManager.getConnection(connectionUrl, username, password);
    }

    @Override
    public List<Person> getAll() throws Exception {
        List<Person> persons = new ArrayList<>();
        String query = "SELECT * FROM person";
        Statement statement = connection.createStatement();
        ResultSet set = statement.executeQuery(query);
        while (set.next()) {
            int id = set.getInt("id");
            String name = set.getString("name");
            String surname = set.getString("surname");
            String address = set.getString("address");
            String phone = set.getString("phone");
            int age = set.getInt("age");
            persons.add(new Person(id, name, surname, address, phone, age));
        }

        return persons;
    }

    @Override
    public void add(String name, String surname, String address, String phone, int age) throws Exception {
        String query = "INSERT INTO person (name, surname, address, phone, age) VALUES (?, ?, ?, ?, ?)";
        try(PreparedStatement statement = createStatement(query, name, surname, address, phone, age)) {
            statement.executeUpdate();
        }
    }

    @Override
    public void edit(int id, String name, String surname, String address, String phone, int age) throws Exception {
        String query = "UPDATE person SET name = ?, surname = ?, address = ?, phone = ?, age = ? WHERE id = ?";
        try (PreparedStatement statement = createStatement(query, name, surname, address, phone, age)) {
            statement.setInt(6, id);
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Person person) throws Exception {
        String query = "DELETE FROM person WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, person.getId());
        statement.executeUpdate();
    }

    @Override
    public void save() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private PreparedStatement createStatement(String query, String name, String surname, String address, String phone, int age) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, name);
        statement.setString(2, surname);
        statement.setString(3, address);
        statement.setString(4, phone);
        statement.setInt(5, age);
        return statement;
    }
}