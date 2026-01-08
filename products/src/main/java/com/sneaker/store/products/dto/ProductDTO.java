package com.sneaker.store.products.dto;

import com.sneaker.store.products.enums.Category;
import com.sneaker.store.products.model.ProductImage;

public record ProductDTO(
        Long id,
        String brand,
        String name,
        String model,
        Category category,
        String article,
        double size,
        double price,
        ProductImage image
) {
}
