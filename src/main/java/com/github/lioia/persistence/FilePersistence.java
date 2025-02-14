package com.github.lioia.persistence;

import com.github.lioia.models.Person;
import com.github.lioia.models.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FilePersistence implements PersistenceLayer {
    // References to the required files
    private final File personsFile, usersFile;
    // In-memory representation of the persons
    private final List<Person> persons;

    public FilePersistence() {
        // Create reference to the two files required
        this.personsFile = new File("informazioni.txt");
        this.usersFile = new File("users.txt");
        if (!this.personsFile.exists()) {
            // Just logging information
            System.out.println("File informazioni.txt does not exist");
        }

        if (!this.usersFile.exists()) {
            // Just logging information
            System.out.println("File users.txt does not exist");
        }

        persons = new ArrayList<>();
        try {
            // Create scanner for the persons file
            Scanner scanner = new Scanner(personsFile);
            // Using line number as id
            int currentLine = 0;
            // Read line by line
            while (scanner.hasNextLine()) {
                // Split for the `;` character
                String[] line = scanner.nextLine().split(";");
                // Get data from the current line
                String name = line[0];
                String lastName = line[1];
                String address = line[2];
                String phone = line[3];
                int age = Integer.parseInt(line[4]);
                // Create new person from the data
                Person person = new Person(currentLine, name, lastName, address, phone, age);
                persons.add(person);
                // Increment for the next iteration
                currentLine += 1;
            }
        } catch (FileNotFoundException e) {
            // No file found; the program continues to work with an empty persons list
            System.out.println("File not found");
        }
    }

    @Override
    public List<Person> getAllPersons() {
        return persons;
    }

    @Override
    public void editPerson(int id, String name, String surname, String address, String phone, int age) {
        // Find the person by the provided id and update the data inside
        persons.stream().filter(person -> person.getId() == id).findFirst().ifPresent(person -> {
            person.setName(name);
            person.setSurname(surname);
            person.setAddress(address);
            person.setPhone(phone);
            person.setAge(age);
        });
    }

    @Override
    public void addPerson(String name, String surname, String address, String phone, int age) {
        // New id corresponds to the size of the persons list
        int id = persons.size();
        // Add new person
        persons.add(new Person(id, name, surname, address, phone, age));
    }

    @Override
    public void save() {
        // Saving in this context requires to update the file by replacing the contents
        if (personsFile.exists()) {
            try (PrintStream stream = new PrintStream(personsFile)) {
                for (Person person : persons) {
                    stream.println(person.toString());
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
            }
        }
    }

    @Override
    public void deletePerson(Person person) {
        persons.remove(person);
    }

    @Override
    public boolean isUserValid(User user) {
        try {
            Scanner scanner = new Scanner(usersFile);
            // Read line by line
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(";");
                String username = line[0];
                String password = line[1];

                if (username.equals(user.username()) && password.equals(user.password())) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        return false;
    }
}