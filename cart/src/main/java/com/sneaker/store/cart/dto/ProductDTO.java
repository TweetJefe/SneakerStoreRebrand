package com.sneaker.store.cart.dto;

import java.util.List;

public record ProductDTO(
        Long id,
        String name,
        String article,
        Double price,
        boolean category,
        String brand,
        List<Double> sizes,
        String model,
        String url
) {
}
