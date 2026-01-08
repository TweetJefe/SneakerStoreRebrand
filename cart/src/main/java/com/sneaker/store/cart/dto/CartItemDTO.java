package com.sneaker.store.cart.dto;

public record CartItemDTO(
        Long itemId,
        String productName,
        int quantity,
        double price
) {
}
