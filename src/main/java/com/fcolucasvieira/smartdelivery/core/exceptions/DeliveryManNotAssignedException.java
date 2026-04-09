package com.fcolucasvieira.smartdelivery.core.exceptions;

public class DeliveryManNotAssignedException extends RuntimeException {
    public DeliveryManNotAssignedException() {
        super("Order has no delivery man assigned");
    }
}
