package com.sneaker.store.users.exceptions;

public class UniquenessViolation extends RuntimeException {
    public UniquenessViolation(String message) {
        super(message);
    }
}
