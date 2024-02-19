package org.example;

import org.example.client.Client;
import org.example.client.Handler;
import org.example.client.impl.ClientTestImpl;
import org.example.client.impl.HandlerImpl;

public class Main {
    public static void main(String[] args) {

        Client client = new ClientTestImpl();
        Handler handler = new HandlerImpl(client);
        System.out.println(handler.performOperation("1121"));
    }
}