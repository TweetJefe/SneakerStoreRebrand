package com.sneaker.store.products.dto;

import com.sneaker.store.products.enums.Category;

public record ProductCriteria(
        String name,
        Category category,
        double price,
        double size,
        String brand,
        String model
){
}
