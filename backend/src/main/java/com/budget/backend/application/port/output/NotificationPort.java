package com.budget.backend.application.port.output;

public interface NotificationPort {

    void info(String message);

    void warn(String message);

    void error(String message);
}
