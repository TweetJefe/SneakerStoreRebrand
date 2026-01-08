package com.sneaker.store.products.exceptions;

public class UniquenessViolation extends RuntimeException {
    public UniquenessViolation(String constraintName) {
        super("Your " + constraintName + " is not unique");
    }
}

