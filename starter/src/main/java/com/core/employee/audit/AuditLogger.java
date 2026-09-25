package com.core.employee.audit;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class AuditLogger {

    public AuditLogger() {
        System.out.println("New AuditLogger instance created");
    }

    public void log(String message) {
        System.out.println(
                "[AUDIT] " + message
        );
    }
}
