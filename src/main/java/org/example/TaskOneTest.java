package org.example;

import org.example.taskone.client.Client;
import org.example.taskone.client.Handler;
import org.example.taskone.client.impl.ClientTestImpl;
import org.example.taskone.client.impl.HandlerImpl;

public class TaskOneTest {
    public static void main(String[] args) {

        Client client = new ClientTestImpl();
        Handler handler = new HandlerImpl(client);
        System.out.println(handler.performOperation("1121"));
    }
}