package com.sneaker.store.users.dto;

import com.sneaker.store.users.model.Address;

public record UserProfileDTO(
        String email,
        String phoneNumber,
        String firstName,
        String lastName,
        Address address
) {
}
