package com.sneaker.store.orders.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.antlr.v4.runtime.misc.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record OrderCreateDTO(
        @NotNull List<String> items,
        @Positive int quantity,
        @NotNull Long customerId,
        @NotBlank String name,
        @Email String email,
        @Pattern(regexp = "\\+?[0-9\\-\\s]+") String phone,
        @Valid AddressDTO address
) {}

