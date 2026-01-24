package com.sneaker.store.cart.dto;

import java.util.List;

public record CartDTO(
        double price,
        List<CartItemDTO> items
) {
}
