package com.sneaker.store.users.dto;

public record UpdatePasswordDTO(
        String newPassword,
        String password
) {
}
