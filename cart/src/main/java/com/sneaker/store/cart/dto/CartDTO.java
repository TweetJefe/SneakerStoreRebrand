package com.sneaker.store.cart.dto;

import java.util.List;

public record CartDTO(
        Long cartId,
        double price,
        List<CartItemDTO> items
) {
}
