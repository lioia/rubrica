package com.github.lioia.persistence;

import com.github.lioia.models.Person;

import java.util.List;

public interface PersistenceLayer {
    void load();
    List<Person> getAll();
    Person getOne(int id); // TODO: check whether this is useful or not
    void add(String name, String surname, String address, String phone, int age);
    void edit(int id, String name, String surname, String address, String phone, int age);
    void delete(Person person);
    void save();
}