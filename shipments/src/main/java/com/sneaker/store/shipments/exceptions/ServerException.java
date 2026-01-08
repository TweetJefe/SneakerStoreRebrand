package com.sneaker.store.shipments.exceptions;

public class ServerException extends RuntimeException {
    public ServerException() {
        super("Server error");
    }
}
