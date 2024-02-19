package org.example.client.impl;

import org.example.client.Client;
import org.example.client.Handler;
import org.example.client.response.ApplicationStatusResponse;
import org.example.client.response.Response;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;

public class HandlerImpl implements Handler {

    private final Client client;
    private Duration lastRequestTime = null;
    private int retries = 0;

    public HandlerImpl(Client client) {
        this.client = client;
    }

    @Override
    public ApplicationStatusResponse performOperation(String id) {
        CompletableFuture<Response> statusOneFuture = CompletableFuture.supplyAsync(() -> client.getApplicationStatus1(id));
        CompletableFuture<Response> statusTwoFuture = CompletableFuture.supplyAsync(() -> client.getApplicationStatus2(id));

        return performOperation(id, statusOneFuture, statusTwoFuture);
    }

    private ApplicationStatusResponse performOperation(String id,
                                                       CompletableFuture<Response> statusOneFuture,
                                                       CompletableFuture<Response> statusTwoFuture) {

        CompletableFuture<Object> result = CompletableFuture.anyOf(statusOneFuture, statusTwoFuture);

        try {
            Response response = (Response) result.get(15, TimeUnit.SECONDS);
            if (response instanceof Response.Success) {
                return new ApplicationStatusResponse.Success(((Response.Success) response).applicationId(),
                        ((Response.Success) response).applicationStatus());

            } else if (response instanceof Response.Failure) {
                lastRequestTime = Duration.ofSeconds(Instant.EPOCH.getEpochSecond());
                return new ApplicationStatusResponse.Failure(lastRequestTime, retries);

            } else if (response instanceof Response.RetryAfter retryAfter) {
                Executor delayed =
                        CompletableFuture.delayedExecutor(retryAfter.delay().get(TimeUnit.SECONDS.toChronoUnit()), TimeUnit.SECONDS);
                CompletableFuture<Response> statusRetryFuture;
                retries++;
                if (statusOneFuture.isDone()) {
                    statusRetryFuture = CompletableFuture.supplyAsync(() -> client.getApplicationStatus1(id), delayed);
                    return performOperation(id, statusRetryFuture, statusTwoFuture);
                } else {
                    statusRetryFuture = CompletableFuture.supplyAsync(() -> client.getApplicationStatus2(id), delayed);
                    return performOperation(id, statusOneFuture, statusRetryFuture);
                }

            } else {
                return new ApplicationStatusResponse.Failure(lastRequestTime, retries);
            }

        } catch (Exception e) {
            return new ApplicationStatusResponse.Failure(lastRequestTime, retries);
        }
    }
}
