package org.example.tasktwo.client.impl;

import org.example.tasktwo.client.Client;
import org.example.tasktwo.client.entity.Address;
import org.example.tasktwo.client.entity.Event;
import org.example.tasktwo.client.entity.Payload;
import org.example.tasktwo.client.entity.Result;

import java.util.List;

public class ClientImpl implements Client {
    @Override
    public Event readData() {
        return new Event(List.of("a1", "a2", "a3", "a4", "a5", "a6", "a7")
                .stream().map(a -> new Address(a, a)).toList(),
                new Payload("test", new byte[10]));
    }

    @Override
    public Result sendData(Address dest, Payload payload) {
        return Math.random() >= 0.5 ? Result.ACCEPTED : Result.REJECTED;
    }
}
