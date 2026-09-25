package com.core.employee.notification;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(1)
public class EmailNotifier implements NotificationInterface{
    @Override
    public void sendMessage(String message) {
        System.out.println("Via Email : "+message);
    }
}
