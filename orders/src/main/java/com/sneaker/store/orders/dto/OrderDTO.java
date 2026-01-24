package com.sneaker.store.orders.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sneaker.store.orders.model.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class OrderDTO {
    private Long id;

    private String orderNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime orderDate;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    private Address address;

    private String orderStatus;

    private List<ProductItemDTO> items = new ArrayList<>();

    private String deliveryMethod;
    private String paymentMethod;
}

