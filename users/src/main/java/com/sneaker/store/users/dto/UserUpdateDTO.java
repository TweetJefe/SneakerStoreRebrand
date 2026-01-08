package com.sneaker.store.users.dto;

import com.sneaker.store.users.model.Address;

public record UserUpdateDTO(
        String email,
        Address address,
        String phoneNumber
) {
}
