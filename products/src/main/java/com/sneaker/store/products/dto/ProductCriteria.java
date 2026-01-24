package com.sneaker.store.products.dto;

import java.util.List;

public record ProductCriteria(
        String name,
        String article,
        Boolean category,
        Double priceFrom,
        Double priceTo,
        List<Double> size,
        List<String> brand,
        String model
){
}
