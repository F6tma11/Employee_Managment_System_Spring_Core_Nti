package com.core.employee.service;


import com.core.employee.audit.AuditLogger;
import com.core.employee.config.CompanyProperties;
import com.core.employee.entity.Employee;
import com.core.employee.exceptions.InvalidEmployeeException;
import com.core.employee.notification.NotificationInterface;
import com.core.employee.notification.NotificationManager;
import com.core.employee.repository.EmployeeRepository;
import com.core.employee.validator.EmployeeValidator;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
@Service
public class EmployeeServiceImp implements EmployeeService {

    private EmployeeRepository employeeRepository;
    private NotificationManager notificationManager;
    private EmployeeValidator validator;
    private final ObjectProvider<AuditLogger> auditLoggerProvider;
    private final CompanyProperties companyProperties;

    public EmployeeServiceImp(EmployeeRepository employeeRepository, NotificationManager notificationManager,
                              EmployeeValidator validator,ObjectProvider<AuditLogger> auditLoggerProvider
                             ,CompanyProperties companyProperties) {
        this.employeeRepository = employeeRepository;
        this.notificationManager = notificationManager;
        this.validator = validator;
        this.auditLoggerProvider=auditLoggerProvider;
        this.companyProperties=companyProperties;
    }

    @Override
    public void addEmployee(Employee employee) {
        validateEmployee(employee);
        employeeRepository.saveEmployee(employee);
        AuditLogger auditLogger =
                auditLoggerProvider.getObject();

        auditLogger.log(
                "Employee added: "
                        + employee.getId()
                        + " - "
                        + employee.getName()
        );
        notificationManager.notifyAll(
                "New employee added: " + employee.getName()
        );
    }

    @Override
    public Employee getEmployeeById(int id) {
        return employeeRepository.getEmployeeById(id);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.getAllEmployees();
    }

    @Override
    public void updateEmployee(Employee employee) {
        validateEmployee(employee);
        employeeRepository.updateEmployee(employee);
    }

    @Override
    public void deleteEmployee(int id) {
        employeeRepository.deleteEmployee(id);
    }

    @Override
    public void giveRaise(int employeeId, double percentage) {
        Employee employee = employeeRepository.getEmployeeById(employeeId);

        if (employee == null) {
            throw new RuntimeException("Employee not found");
        }

        if (percentage < 0) {
            throw new InvalidEmployeeException(
                    "Raise percentage cannot be negative"
            );
        }


        if (percentage > companyProperties.getMaxRaisePercentage()) {

            throw new InvalidEmployeeException(
                    "Raise percentage cannot exceed "
                            + companyProperties.getMaxRaisePercentage()
                            + "%"
            );
        }

        double oldSalary = employee.getSalary();
        double increase = employee.getSalary() * percentage / 100;
        employee.setSalary(employee.getSalary() + increase);


        validateEmployee(employee);
        employeeRepository.updateEmployee(employee);
        AuditLogger auditLogger =
                auditLoggerProvider.getObject();

        auditLogger.log(
                "Employee "
                        + employee.getName()
                        + " received "
                        + percentage
                        + "% raise. Old salary: "
                        + oldSalary
                        + ", New salary: "
                        + employee.getSalary()
        );
        notificationManager.notifyAll(
                "Employee " + employee.getName()
                        + " received a " + percentage + "% raise"
        );
    }

    private void validateEmployee(Employee employee) {

        Errors errors =
                new BeanPropertyBindingResult(
                        employee,
                        "employee"
                );

        validator.validate(employee, errors);

        if (errors.hasErrors()) {

            StringBuilder message = new StringBuilder();

            errors.getAllErrors().forEach(error ->
                    message.append(error.getDefaultMessage())
                            .append("\n")
            );

            throw new InvalidEmployeeException(
                    message.toString()
            );
        }
    }

    @PostConstruct
    public void init() {
        System.out.println("EmployeeService initialized");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("EmployeeService destroyed");
    }
}
