package com.core.employee.repository;
import com.core.employee.entity.Employee;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository {

    void saveEmployee(Employee employee);

    void updateEmployee(Employee employee);

    void deleteEmployee(int id);

    List<Employee> getAllEmployees();

    Employee getEmployeeById(int id);
}
