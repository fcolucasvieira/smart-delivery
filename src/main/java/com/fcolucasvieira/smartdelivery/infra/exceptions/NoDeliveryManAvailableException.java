package com.fcolucasvieira.smartdelivery.infra.exceptions;

public class NoDeliveryManAvailableException extends RuntimeException {
    public NoDeliveryManAvailableException() {
        super("No delivery man available");
    }
}
