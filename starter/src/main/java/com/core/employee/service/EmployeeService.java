package com.core.employee.service;

import com.core.employee.entity.Employee;
import com.core.employee.exceptions.InvalidEmployeeException;
import com.core.employee.repository.EmployeeRepository;
import com.core.employee.validator.EmployeeValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmployeeService {

    void addEmployee(Employee employee);

    Employee getEmployeeById(int id);

    List<Employee> getAllEmployees();

    void updateEmployee(Employee employee);

    void deleteEmployee(int id);

    void giveRaise(int employeeId, double percentage);
}
