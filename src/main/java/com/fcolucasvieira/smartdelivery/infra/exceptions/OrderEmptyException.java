package com.fcolucasvieira.smartdelivery.infra.exceptions;

public class OrderEmptyException extends RuntimeException {
    public OrderEmptyException() {
        super("Order must have at least one item");
    }
}
