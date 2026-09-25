package com.core.employee.notification;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationManager {

    private final List<NotificationInterface> notifiers;

    public NotificationManager(List<NotificationInterface> notifiers){
        this.notifiers=notifiers;
    }

    public void notifyAll(String message) {

        for (NotificationInterface notifier : notifiers) {
            notifier.sendMessage(message);
        }
    }
}
