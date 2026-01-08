package com.sneaker.store.orders.exceptions;

public class NullableViolation extends RuntimeException {
    public NullableViolation() {
        super("You have to fill all of the fields!");
    }
}
