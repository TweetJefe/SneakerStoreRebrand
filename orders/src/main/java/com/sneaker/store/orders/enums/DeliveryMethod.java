package com.sneaker.store.orders.enums;

import lombok.Getter;

@Getter
public enum DeliveryMethod {
    GLS("GLS"),
    DHL("DHL"),
    UPS("UPS");

    private final String value;

    DeliveryMethod(String value) {
        this.value = value;
    }
}
