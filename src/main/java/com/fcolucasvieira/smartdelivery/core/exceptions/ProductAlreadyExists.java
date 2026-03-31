package com.fcolucasvieira.smartdelivery.core.exceptions;

public class ProductAlreadyExists extends RuntimeException {
    public ProductAlreadyExists(String message) {
        super(message);
    }
}
