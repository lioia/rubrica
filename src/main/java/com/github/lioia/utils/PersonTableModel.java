package com.github.lioia.utils;

import com.github.lioia.models.Person;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class PersonTableModel extends AbstractTableModel {
    private final List<Person> persons;

    public PersonTableModel(List<Person> persons) {
        this.persons = persons;
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> "Name";
            case 1 -> "Surname";
            case 2 -> "Phone";
            default -> throw new IllegalStateException("Unexpected column value: " + column);
        };
    }

    @Override
    public int getRowCount() {
        return persons.size(); // Number of persons saved
    }

    @Override
    public int getColumnCount() {
        return 3; // Just name, surname and age
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Person person = persons.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> person.getName();
            case 1 -> person.getSurname();
            case 2 -> person.getPhone();
            default -> throw new IllegalStateException("Unexpected column index value: " + columnIndex);
        };
    }
}
