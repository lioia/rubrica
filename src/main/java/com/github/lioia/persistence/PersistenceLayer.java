package com.github.lioia.persistence;

import com.github.lioia.models.Person;

import java.sql.SQLException;
import java.util.List;

public interface PersistenceLayer {
    List<Person> getAll() throws Exception;
    void add(String name, String surname, String address, String phone, int age) throws Exception;
    void edit(int id, String name, String surname, String address, String phone, int age) throws Exception;
    void delete(Person person) throws Exception;
    void save();
}