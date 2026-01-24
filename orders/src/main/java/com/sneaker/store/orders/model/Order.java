package com.sneaker.store.orders.model;


import com.sneaker.store.orders.enums.DeliveryMethod;
import com.sneaker.store.orders.enums.OrderStatus;
import com.sneaker.store.orders.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String orderNumber;

    private LocalDateTime orderDate;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    @Embedded
    private Address address;

    @Enumerated
    private OrderStatus orderStatus;

    @Enumerated
    private DeliveryMethod deliveryMethod;

    @Enumerated
    private PaymentMethod paymentMethod;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<ProductItem> items = new ArrayList<>();
}
