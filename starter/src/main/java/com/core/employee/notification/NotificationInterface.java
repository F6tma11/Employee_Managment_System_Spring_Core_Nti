package com.core.employee.notification;

import org.springframework.stereotype.Service;

@Service
public interface NotificationInterface {

    void sendMessage(String message);
}
