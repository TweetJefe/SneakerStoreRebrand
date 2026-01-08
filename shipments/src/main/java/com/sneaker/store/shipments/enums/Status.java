package com.sneaker.store.shipments.enums;

public enum Status {
    PENDING, // ожидает подтверждения/отправки
    IN_TRANSIT, // в пути
    DELIVERED, // доставлен
    CANCELLED
}