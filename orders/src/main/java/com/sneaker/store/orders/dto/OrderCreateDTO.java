package com.sneaker.store.orders.dto;

import com.sneaker.store.orders.model.Address;
import jakarta.validation.constraints.NotBlank;
import org.antlr.v4.runtime.misc.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record OrderCreateDTO(
        @NotNull List<ProductItemDTO> items,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Pattern(regexp = "\\+?[0-9\\-\\s]+") String phone,
        Address address,
        String delivery,
        boolean payment
) {}

