package com.sneaker.store.orders.exceptions;

public class ServerException extends RuntimeException {
    public ServerException() {
        super("Server error");
    }
}
