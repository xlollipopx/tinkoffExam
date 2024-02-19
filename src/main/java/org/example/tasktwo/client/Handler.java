package org.example.tasktwo.client;

import java.time.Duration;

public interface Handler {
    Duration timeout();

    void performOperation();
}