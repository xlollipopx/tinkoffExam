package org.example.client;

import org.example.client.response.ApplicationStatusResponse;

public interface Handler {
    ApplicationStatusResponse performOperation(String id);
}