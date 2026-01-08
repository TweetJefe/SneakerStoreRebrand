package com.sneaker.store.products.exceptions;

public class ServerException extends RuntimeException {
  public ServerException() {
    super("Server error");
  }
}