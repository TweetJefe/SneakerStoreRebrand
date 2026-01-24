package com.sneaker.store.users.dto;

import com.sneaker.store.users.model.Address;
import jakarta.persistence.Embedded;

public record UserCreateDTO(
         String firstName,
         String lastName,
         String email,
         String password,
         String phoneNumber,
         Address address,
         String role

) {

}
