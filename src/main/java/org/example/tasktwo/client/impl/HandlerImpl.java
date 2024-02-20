package org.example.tasktwo.client.impl;

import org.example.tasktwo.client.Client;
import org.example.tasktwo.client.Handler;
import org.example.tasktwo.client.entity.Address;
import org.example.tasktwo.client.entity.Event;
import org.example.tasktwo.client.entity.Payload;
import org.example.tasktwo.client.entity.Result;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class HandlerImpl implements Handler {

    private final Client client;
    private final Executor delayedExecutor;

    public HandlerImpl(Client client) {
        this.client = client;
        this.delayedExecutor =
                CompletableFuture.delayedExecutor(timeout().get(TimeUnit.SECONDS.toChronoUnit()), TimeUnit.SECONDS);
    }

    @Override
    public Duration timeout() {
        return Duration.ofSeconds(3);
    }

    @Override
    public void performOperation() {
        Event event = client.readData();
        List<CompletableFuture<Result>> results = event.recipients().
                stream().map(r -> CompletableFuture.supplyAsync(() -> client.sendData(r, event.payload()))
                        .whenCompleteAsync(
                                (res, error) -> {
                                    if (error != null || res == Result.REJECTED) {
                                        retry(r, event.payload());
                                    } else {
                                        System.out.println("Node " + r.datacenter() + " received the message.");
                                    }
                                })).toList();

        results.forEach(CompletableFuture::join);
    }

    private void retry(Address address, Payload payload) {
        System.out.println("Node " + address.datacenter() + " rejected message, retry in " + timeout().getSeconds() + " seconds.");
        CompletableFuture<Result> retryFuture = CompletableFuture.supplyAsync(() -> client.sendData(address, payload), delayedExecutor)
                .whenComplete(
                        (res, error) -> {
                            if (error != null || res == Result.REJECTED) {
                                retry(address, payload);
                            } else {
                                System.out.println("Node " + address.datacenter() + " received the message.");
                            }
                        });

        retryFuture.join();
    }

}
