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
    private final File personsFile, usersFile;
    private final List<Person> persons;

    public FilePersistence() {
        this.personsFile = new File("informazioni.txt");
        this.usersFile = new File("users.txt");
        if (!this.personsFile.exists()) {
            System.out.println("File informazioni.txt does not exist");
        }

        if (!this.usersFile.exists()) {
            System.out.println("File users.txt does not exist");
        }

        persons = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(personsFile);
            int currentLine = 0;
            // Read line by line
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(";");
                String name = line[0];
                String lastName = line[1];
                String address = line[2];
                String phone = line[3];
                int age = Integer.parseInt(line[4]);
                // Using currenLine as person id
                Person person = new Person(currentLine, name, lastName, address, phone, age);
                persons.add(person);
                currentLine += 1;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    @Override
    public List<Person> getAllPersons() {
        return persons;
    }

    @Override
    public void editPerson(int id, String name, String surname, String address, String phone, int age) {
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
        int id = persons.size();
        persons.add(new Person(id, name, surname, address, phone, age));
    }

    @Override
    public void save() {
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