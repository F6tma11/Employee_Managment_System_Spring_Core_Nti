# Employee Management System

A simple **Employee Management System** built using **Spring Core** without Spring Boot.
The project demonstrates dependency injection, bean scopes, profiles, validation, notifications, external properties, and lifecycle management.

## Technologies

* Java
* Spring Core
* Spring Context
* Spring Validation
* Maven
* File-based storage

## Project Structure

```text
com.core.employee
├── audit
│   └── AuditLogger
├── config
│   ├── AppConfiguration
│   └── CompanyProperties
├── entity
│   └── Employee
├── exception
│   └── InvalidEmployeeException
├── notification
│   ├── Notifier
│   ├── EmailNotifier
│   ├── SmsNotifier
│   ├── PushNotifier
│   └── NotificationManager
├── repository
│   ├── EmployeeRepository
│   ├── InMemoryEmployeeRepository
│   └── FileBackedEmployeeRepository
├── service
│   ├── EmployeeService
│   └── EmployeeServiceImpl
└── validator
    └── EmployeeValidator
```

## Main Features

### 1. Employee Management

The system supports:

* Add employee
* Find employee by ID
* Get all employees
* Update employee
* Delete employee
* Give employee a salary raise

The `Employee` entity contains:

```text
id
name
department
salary
```

### 2. Repository Abstraction

The application uses an `EmployeeRepository` interface with two implementations:

* `InMemoryEmployeeRepository`
* `FileBackedEmployeeRepository`

The service depends only on the interface, not on a specific implementation.

This allows the repository implementation to be changed without modifying the service.

### 3. Spring Profiles

Two Spring profiles are used:

```text
dev  → InMemoryEmployeeRepository
prod → FileBackedEmployeeRepository
```

The active profile is selected from `Main`.

For example:

```java
context.getEnvironment().setActiveProfiles("dev");
```

or:

```java
context.getEnvironment().setActiveProfiles("prod");
```

The `prod` profile stores employees in a text file, while the `dev` profile keeps them in memory.

### 4. Dependency Injection

The project uses **constructor injection** for the main application components.

For example, `EmployeeServiceImpl` receives:

* `EmployeeRepository`
* `NotificationManager`
* `EmployeeValidator`
* `ObjectProvider<AuditLogger>`
* `CompanyProperties`

The application does not manually create Spring-managed services, repositories, or notification objects using `new`.

### 5. Notification System

The project defines a common `Notifier` interface with three implementations:

* Email
* SMS
* Push

`NotificationManager` receives all notifier implementations as:

```java
List<Notifier>
```

The notification order is controlled using `@Order`.

When an employee is added or receives a raise, all configured notification channels are triggered.

### 6. Validation

`EmployeeValidator` implements Spring's `Validator` interface.

It validates:

* Employee name is not empty
* Department is not empty
* Salary is not negative

Validation errors are converted into `InvalidEmployeeException` by the service layer.

### 7. Singleton and Prototype Scopes

Spring beans are singleton by default.

`EmployeeServiceImpl` is therefore a **Singleton** bean.

`AuditLogger`, however, is configured as a **Prototype** bean:

```java
@Component
@Scope("prototype")
public class AuditLogger {
}
```

The singleton service uses:

```java
ObjectProvider<AuditLogger>
```

to request a new `AuditLogger` instance whenever it needs one.

This avoids keeping one prototype instance inside the singleton service.

### 8. External Properties

Application configuration is stored in:

```text
src/main/resources/application.properties
```

Example:

```properties
company.name=My Company
company.currency=EGP
notification.retry-count=3
raise.max-percentage=20
```

The values are injected using Spring's `@Value`.

The maximum allowed raise percentage is read from:

```properties
raise.max-percentage
```

and the service rejects raises above this value.

### 9. Bean Lifecycle

The service demonstrates Spring bean lifecycle callbacks using:

```java
@PostConstruct
```

and:

```java
@PreDestroy
```

`@PostConstruct` runs after the service bean is initialized.

`@PreDestroy` runs when the Spring application context is closed.

The context is explicitly closed in `Main`:

```java
context.close();
```

## Running the Application

1. Build the project using Maven.
2. Run the `Main` class.
3. Select the required Spring profile:

```java
context.getEnvironment().setActiveProfiles("dev");
```

or:

```java
context.getEnvironment().setActiveProfiles("prod");
```

4. The application demonstrates:

    * Adding valid employees
    * Validation of invalid employees
    * Finding employees
    * Listing employees
    * Giving a valid raise
    * Rejecting an excessive raise
    * Sending notifications
    * Creating prototype `AuditLogger` instances
    * Reading application properties
    * Spring lifecycle callbacks

## Key Spring Concepts Demonstrated

| Concept                | Implementation                        |
| ---------------------- | ------------------------------------- |
| IoC Container          | `AnnotationConfigApplicationContext`  |
| Component Scanning     | `@ComponentScan`                      |
| Constructor Injection  | `EmployeeServiceImpl`                 |
| Profiles               | `@Profile("dev")`, `@Profile("prod")` |
| Singleton              | `EmployeeServiceImpl`                 |
| Prototype              | `AuditLogger`                         |
| Lazy Prototype Lookup  | `ObjectProvider<AuditLogger>`         |
| Collection Injection   | `List<Notifier>`                      |
| Ordering               | `@Order`                              |
| Validation             | Spring `Validator`                    |
| External Configuration | `@PropertySource`, `@Value`           |
| Lifecycle              | `@PostConstruct`, `@PreDestroy`       |

## Important Design Principle

The application follows the principle of **programming to interfaces**.

For example:

```text
EmployeeService
      ↓
EmployeeRepository
      ↓
 ┌────┴─────┐
 ↓          ↓
Memory     File
```

The service does not need to know which repository implementation is being used. Spring manages the dependency and selects the implementation according to the active profile.
