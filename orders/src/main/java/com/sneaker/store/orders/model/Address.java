package com.sneaker.store.orders.model;

import jakarta.persistence.*;
import lombok.*;


@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Address {
    private String address;
    private String city;
    private String state;
    private String zip;
    private String country;
}