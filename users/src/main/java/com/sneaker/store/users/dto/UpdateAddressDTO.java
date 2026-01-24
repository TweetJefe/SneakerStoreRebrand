package com.sneaker.store.users.dto;

public record UpdateAddressDTO(
        String city,
        String street,
        String zipcode,
        String country,
        String houseNumber,
        String doorNumber
) {
}
