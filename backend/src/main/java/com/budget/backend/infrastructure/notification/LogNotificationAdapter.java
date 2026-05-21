package com.budget.backend.infrastructure.notification;

import com.budget.backend.application.port.output.NotificationPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogNotificationAdapter implements NotificationPort {

    @Override
    public void info(String message) {
        log.info("[NOTIFICATION] {}", message);
    }

    @Override
    public void warn(String message) {
        log.warn("[NOTIFICATION] {}", message);
    }

    @Override
    public void error(String message) {
        log.error("[NOTIFICATION] {}", message);
    }
}
