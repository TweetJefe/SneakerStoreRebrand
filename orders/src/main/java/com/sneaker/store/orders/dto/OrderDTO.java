package com.sneaker.store.orders.dto;

import com.sneaker.store.orders.enums.OrderStatus;
import com.sneaker.store.orders.model.Address;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDTO (
        String orderNumber,
        List<String> items,
        int quantity,
        LocalDateTime orderDate,
        String name,
        String email,
        String phone,
        Address address,
        OrderStatus orderStatus
) {}

