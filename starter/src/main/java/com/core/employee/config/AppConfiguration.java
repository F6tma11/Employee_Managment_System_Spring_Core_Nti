package com.core.employee.config;

import com.core.employee.notification.EmailNotifier;
import com.core.employee.notification.NotificationInterface;
import com.core.employee.notification.PushNotifier;
import com.core.employee.notification.SmsNotifier;
import com.core.employee.repository.InMemoryEmployeeRepository;
import com.core.employee.repository.EmployeeRepository;
import com.core.employee.service.*;
import com.core.employee.validator.EmployeeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;

@Configuration
@ComponentScan("com.core.employee")
@PropertySource("classpath:application.properties")
public class AppConfiguration {






}
