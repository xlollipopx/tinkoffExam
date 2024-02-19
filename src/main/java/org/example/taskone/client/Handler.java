package org.example.taskone.client;

import org.example.taskone.client.response.ApplicationStatusResponse;

public interface Handler {
    ApplicationStatusResponse performOperation(String id);
}