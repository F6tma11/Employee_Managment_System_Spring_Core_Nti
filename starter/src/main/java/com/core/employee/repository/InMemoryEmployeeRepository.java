package com.core.employee.repository;

import com.core.employee.entity.Employee;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Profile("dev")
public class InMemoryEmployeeRepository implements EmployeeRepository {

    private List<Employee> employees;

    public InMemoryEmployeeRepository(List<Employee> employee){
        this.employees=employee;
    }

    @Override
    public void saveEmployee(Employee employee) {
        employees.add(employee);
    }

    @Override
    public void updateEmployee(Employee employee) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId()==employee.getId()){
                employees.set(i,employee);
            }
        }

    }

    @Override
    public void deleteEmployee(int employeeId) {
        employees.remove(employeeId);
    }


    @Override
    public List<Employee> getAllEmployees() {
        ArrayList<Employee> copyEmployee=new ArrayList<>();
        copyEmployee.addAll(employees);
        return copyEmployee;
    }

    @Override
    public Employee getEmployeeById(int id) {

        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId()==id){
                return employees.get(i);
            }
        }
        return null;
    }
}
