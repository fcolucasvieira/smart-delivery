package com.fcolucasvieira.smartdelivery.core.exceptions;

public class NoDeliveryManAvailableException extends RuntimeException {
    public NoDeliveryManAvailableException() {
        super("No delivery man available");
    }
}
