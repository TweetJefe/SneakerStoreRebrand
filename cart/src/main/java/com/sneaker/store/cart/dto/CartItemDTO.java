package com.sneaker.store.cart.dto;

public record CartItemDTO(
        Long id,
        Long productId,
        String name,
        String brand,
        double size,
        String imageUrl,
        int quantity,
        double price
) {
}
