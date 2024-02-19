package org.example;

import org.example.tasktwo.client.Client;
import org.example.tasktwo.client.Handler;
import org.example.tasktwo.client.impl.ClientImpl;
import org.example.tasktwo.client.impl.HandlerImpl;

public class TaskTwoTest {
    public static void main(String[] args) {

        Client client = new ClientImpl();

        Handler handler = new HandlerImpl(client);

        handler.performOperation();
    }
}
