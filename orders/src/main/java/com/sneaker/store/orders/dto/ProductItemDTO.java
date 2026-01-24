package com.sneaker.store.orders.dto;

public record ProductItemDTO(
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
