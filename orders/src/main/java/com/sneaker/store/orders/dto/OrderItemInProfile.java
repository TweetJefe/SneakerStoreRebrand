package com.sneaker.store.orders.dto;

import java.time.LocalDate;

public record OrderItemInProfile(
        String orderNumber,
        Double totalPrice,
        LocalDate createdAt,
        String orderStatus
) {
}
