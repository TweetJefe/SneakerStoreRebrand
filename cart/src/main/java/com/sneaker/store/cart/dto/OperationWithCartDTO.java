package com.sneaker.store.cart.dto;

public record OperationWithCartDTO(
        Long cartId,
        Long productId
) {
}
