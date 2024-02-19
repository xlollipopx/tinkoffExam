package org.example.tasktwo.client;

import org.example.tasktwo.client.entity.Address;
import org.example.tasktwo.client.entity.Event;
import org.example.tasktwo.client.entity.Payload;
import org.example.tasktwo.client.entity.Result;

public interface Client {
    //блокирующий метод для чтения данных
    Event readData();

    //блокирующий метод отправки данных
    Result sendData(Address dest, Payload payload);
}