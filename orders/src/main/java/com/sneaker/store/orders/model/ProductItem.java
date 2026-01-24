package com.sneaker.store.orders.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private Long productId;
    private String name;
    private String brand;
    private String imageUrl;
    private int quantity;
    private double size;
    private double price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order order;

    public ProductItem(Long productId, String name, String brand, String imageUrl, int quantity, double size, double price) {
        this.productId = productId;
        this.name = name;
        this.brand = brand;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.size = size;
        this.price = price;
    }
}
