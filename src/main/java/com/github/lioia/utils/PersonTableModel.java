package com.github.lioia.utils;

import com.github.lioia.models.Person;
import com.github.lioia.persistence.PersistenceLayer;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class PersonTableModel extends AbstractTableModel {
    private final PersistenceLayer persistence;
    private List<Person> persons;

    public PersonTableModel(PersistenceLayer persistence) {
        this.persistence = persistence;
        try {
            this.persons = persistence.getAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.persons = new ArrayList<>();
        }
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

    @Override
    public void fireTableDataChanged() {
        try {
            persons = persistence.getAll();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        super.fireTableDataChanged();
    }
}
