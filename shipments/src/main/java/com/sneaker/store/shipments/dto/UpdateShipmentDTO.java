package com.sneaker.store.shipments.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sneaker.store.shipments.enums.Status;

import java.time.LocalDateTime;

public record UpdateShipmentDTO(
        Status status,
        String orderNumber,
        @JsonFormat(pattern = "dd-MM-dd HH:mm:ss")
        LocalDateTime updatedAt) {
}