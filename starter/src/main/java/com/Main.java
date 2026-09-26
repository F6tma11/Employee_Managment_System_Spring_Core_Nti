package com;

import com.core.employee.audit.AuditLogger;
import com.core.employee.config.AppConfiguration;
import com.core.employee.config.CompanyProperties;
import com.core.employee.entity.Employee;

import com.core.employee.exceptions.InvalidEmployeeException;
import com.core.employee.service.EmployeeService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();

        // =========================
        // 1. Select active profile
        // =========================

        context.getEnvironment()
                .setActiveProfiles("dev");



        context.register(AppConfiguration.class);

        context.refresh();


        // =========================
        // 2. Get EmployeeService
        // =========================

        EmployeeService employeeService =
                context.getBean(EmployeeService.class);


        // =========================
        // 3. Get properties
        // =========================

        CompanyProperties properties =
                context.getBean(CompanyProperties.class);

        System.out.println("\n===== COMPANY INFORMATION =====");

        System.out.println(
                "Company: "
                        + properties.getCompanyName()
        );

        System.out.println(
                "Currency: "
                        + properties.getCurrency()
        );

        System.out.println(
                "Notification retry count: "
                        + properties.getNotificationRetryCount()
        );

        System.out.println(
                "Maximum raise: "
                        + properties.getMaxRaisePercentage()
                        + "%"
        );


        // =========================
        // 4. Add valid employee
        // =========================

        System.out.println("\n===== ADD VALID EMPLOYEE =====");

        Employee employee1 =
                new Employee(
                        1,
                        "Fatma Ahmed",
                        "Software Engineer",
                        40000
                );

        employeeService.addEmployee(employee1);


        // =========================
        // 5. Add another employee
        // =========================

        Employee employee2 =
                new Employee(
                        2,
                        "Ahmed Ali",
                        "HR",
                        30000
                );

        employeeService.addEmployee(employee2);


        // =========================
        // 6. Try invalid employee
        // =========================

        System.out.println("\n===== INVALID EMPLOYEE =====");

        Employee invalidEmployee =
                new Employee(
                        3,
                        "",
                        "",
                        -5000
                );

        try {

            employeeService.addEmployee(
                    invalidEmployee
            );

        } catch (InvalidEmployeeException e) {

            System.out.println(
                    "Validation failed:"
            );

            System.out.println(
                    e.getMessage()
            );
        }


        // =========================
        // 7. Get employee by ID
        // =========================

        System.out.println("\n===== FIND EMPLOYEE =====");

        Employee foundEmployee =
                employeeService.getEmployeeById(1);

        System.out.println(foundEmployee);


        // =========================
        // 8. Get all employees
        // =========================

        System.out.println("\n===== ALL EMPLOYEES =====");

        employeeService
                .getAllEmployees()
                .forEach(System.out::println);


        // =========================
        // 9. Valid raise
        // =========================

        System.out.println("\n===== VALID RAISE =====");

        try {

            employeeService.giveRaise(1, 10);

        } catch (InvalidEmployeeException e) {

            System.out.println(
                    "Raise failed: "
                            + e.getMessage()
            );
        }


        // =========================
        // 10. Excessive raise
        // =========================

        System.out.println("\n===== EXCESSIVE RAISE =====");

        try {

            employeeService.giveRaise(1, 30);

        } catch (InvalidEmployeeException e) {

            System.out.println(
                    "Raise failed: "
                            + e.getMessage()
            );
        }


        // =========================
        // 11. Prototype demonstration
        // =========================

        System.out.println("\n===== PROTOTYPE DEMO =====");

        AuditLogger logger1 =
                context.getBean(AuditLogger.class);

        AuditLogger logger2 =
                context.getBean(AuditLogger.class);

        System.out.println(
                "logger1 == logger2 : "
                        + (logger1 == logger2)
        );


        // =========================
        // 12. Final employees
        // =========================

        System.out.println("\n===== FINAL EMPLOYEES =====");

        employeeService
                .getAllEmployees()
                .forEach(System.out::println);


        // =========================
        // 13. Close context
        // =========================

        System.out.println("\n===== CLOSING CONTEXT =====");

        context.close();
    }
}