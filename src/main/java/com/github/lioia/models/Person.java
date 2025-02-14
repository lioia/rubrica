package com.github.lioia.models;

public class Person {
    private int id;
    private String name;
    private String surname;
    private String address;
    private String phone;
    private int age;

    public Person() {
    }

    public Person(int id, String name, String surname, String address, String phone, int age) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.phone = phone;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // Formatting as required by the file persistence layer
    @Override
    public String toString() {
        return name + ";" + surname + ";" + address + ";" + phone + ";" + age;
    }
}