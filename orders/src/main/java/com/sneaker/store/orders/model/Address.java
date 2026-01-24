package com.sneaker.store.orders.model;

import jakarta.persistence.Embeddable;

@Embeddable
public record Address(
        String country,
        String city,
        String street,
        String zipcode,
        String houseNumber,
        String doorNumber
) {
}