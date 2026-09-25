package com.core.employee.notification;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(2)
public class SmsNotifier implements NotificationInterface{
    @Override
    public void sendMessage(String message) {
        System.out.println("Via SMS : "+message);
    }
}
