package com.sneaker.store.orders.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    ONLINE("Online payment"),
    UPON_RECEIPT("Upon receipt payment");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }
}
