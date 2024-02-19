package org.example.taskone.client.impl;

import org.example.taskone.client.Client;
import org.example.taskone.client.response.Response;

import java.time.Duration;

public class ClientTestImpl implements Client {
    @Override
    public Response getApplicationStatus1(String id) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new Response.Success("Ok.", "1");
    }

    @Override
    public Response getApplicationStatus2(String id) {
        if (Math.random() > 0.5) {
            System.out.println("getApplicationStatus2() - call");
            return new Response.RetryAfter(Duration.ofSeconds(1));
        } else {
            return new Response.Success("Succeed", "123");
        }
    }
}
