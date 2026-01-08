package com.sneaker.store.shipments.dto;

import java.util.List;

public record GetShipmentDTO (
        String orderNumber,
        List<String> items,
        Integer quantity
){
}