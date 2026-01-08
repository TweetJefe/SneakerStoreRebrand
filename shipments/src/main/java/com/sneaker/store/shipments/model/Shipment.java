package com.sneaker.store.shipments.model;

import com.sneaker.store.shipments.enums.DeliveryType;
import com.sneaker.store.shipments.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "shipments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String orderNumber;
    private String shipmentNumber;

    private String recipientName;
    private String recipientEmail;
    private String recipientPhone;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime estimatedDeliveryDate;

    @ElementCollection
    private List<String> items;
    private int quantity;

    @Embedded
    private Address address;

    private Status status;
    private DeliveryType deliveryType;

    private String notes;
}
