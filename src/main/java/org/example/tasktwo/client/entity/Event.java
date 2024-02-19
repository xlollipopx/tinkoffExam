package org.example.tasktwo.client.entity;

import java.util.List;

public record Event(List<Address> recipients, Payload payload) {}

