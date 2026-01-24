package com.sneaker.store.products.dto;

import com.sneaker.store.products.model.ProductImage;

import java.util.List;

public record UpdateProductDTO(
        Long id,
        String brand,
        String name,
        String model,
        boolean category,
        String article,
        List<Double> size,
        double price,
        ProductImage image
) {
}