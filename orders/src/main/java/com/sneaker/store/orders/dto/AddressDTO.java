package com.sneaker.store.orders.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressDTO(
        @NotBlank String address,
        @NotBlank String city,
        @NotBlank String state,
        @NotBlank String zip,
        @NotBlank String country
) {
}
