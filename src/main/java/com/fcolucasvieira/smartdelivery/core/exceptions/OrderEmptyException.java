package com.fcolucasvieira.smartdelivery.core.exceptions;

public class OrderEmptyException extends RuntimeException {
    public OrderEmptyException() {
        super("Order must have at least one item");
    }
}
