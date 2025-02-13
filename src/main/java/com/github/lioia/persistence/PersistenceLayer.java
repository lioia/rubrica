package com.github.lioia.persistence;

import com.github.lioia.models.Person;
import com.github.lioia.models.User;

import java.util.List;

public interface PersistenceLayer {
    List<Person> getAllPersons() throws Exception;
    void addPerson(String name, String surname, String address, String phone, int age) throws Exception;
    void editPerson(int id, String name, String surname, String address, String phone, int age) throws Exception;
    void deletePerson(Person person) throws Exception;
    boolean isUserValid(User user);
    void save();
}