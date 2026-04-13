package com.fcolucasvieira.smartdelivery.infra.exceptions;

public class InvalidStatusOrderException extends RuntimeException {
    public InvalidStatusOrderException() {
        super("Order must be is EM_ROTA to be completed");
    }
}
