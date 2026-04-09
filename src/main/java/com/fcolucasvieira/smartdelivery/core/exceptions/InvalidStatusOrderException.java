package com.fcolucasvieira.smartdelivery.core.exceptions;

public class InvalidStatusOrderException extends RuntimeException {
    public InvalidStatusOrderException() {
        super("Order must be is EM_ROTA to be completed");
    }
}
