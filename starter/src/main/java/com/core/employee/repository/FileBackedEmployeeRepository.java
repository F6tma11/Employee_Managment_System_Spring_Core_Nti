package com.core.employee.repository;

import com.core.employee.entity.Employee;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@Repository
@Profile("prod")
public class FileBackedEmployeeRepository implements EmployeeRepository {
    private final Path EMPLOYEES_DATA= Paths.get("employees.txt");
    @Override
    public void saveEmployee(Employee employee) {
        List<Employee> employees = readEmployees();

        employees.add(employee);

        writeEmployees(employees);
    }

    @Override
    public void updateEmployee(Employee employee) {
        List<Employee> employees = readEmployees();

        boolean updated = false;

        for (int i = 0; i < employees.size(); i++) {

            if (employees.get(i).getId() == employee.getId()) {

                employees.set(i, employee);
                updated = true;
                break;
            }
        }

        if (!updated) {
            throw new RuntimeException(
                    "Employee with id " + employee.getId() + " not found"
            );
        }

        writeEmployees(employees);
    }

    @Override
    public void deleteEmployee(int id) {
        List<Employee> employees = readEmployees();

        boolean removed = employees.removeIf(
                employee -> employee.getId() == id
        );

        if (!removed) {
            throw new RuntimeException(
                    "Employee with id " + id + " not found"
            );
        }

        writeEmployees(employees);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return readEmployees();
    }

    @Override
    public Employee getEmployeeById(int id) {
        List<Employee> employees = readEmployees();

        for (Employee employee : employees) {

            if (employee.getId() == id) {
                return employee;
            }
        }

        return null;
    }

    // Read all employees from file
    private List<Employee> readEmployees() {

        List<Employee> employees = new ArrayList<>();

        if (!Files.exists(EMPLOYEES_DATA)) {
            return employees;
        }

        try {

            List<String> lines = Files.readAllLines(EMPLOYEES_DATA);

            for (String line : lines) {

                if (!line.isBlank()) {
                    employees.add(fromLine(line));
                }
            }

        } catch (IOException e) {

            throw new RuntimeException(
                    "Could not read employees file",
                    e
            );
        }

        return employees;
    }

    // Employee -> String
    private String toLine(Employee employee) {

        return employee.getId()
                + "|" + employee.getName()
                + "|" + employee.getDepartment()
                + "|" + employee.getSalary();
    }

    // String -> Employee
    private Employee fromLine(String line) {

        String[] data = line.split("\\|");

        int id = Integer.parseInt(data[0]);
        String name = data[1];
        String department = data[2];
        double salary = Double.parseDouble(data[3]);

        return new Employee(
                id,
                name,
                department,
                salary
        );
    }
    // Write all employees to file
    private void writeEmployees(List<Employee> employees) {

        List<String> lines = new ArrayList<>();

        for (Employee employee : employees) {
            lines.add(toLine(employee));
        }

        try {

            Files.write(
                    EMPLOYEES_DATA,
                    lines,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Could not write employees file",
                    e
            );
        }
    }

}
