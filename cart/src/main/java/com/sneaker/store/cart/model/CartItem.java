package com.sneaker.store.cart.model;

import com.sneaker.store.cart.dto.ProductDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private Long productId;
    private String name;
    private String brand;
    private Double size;
    private String imageUrl;
    private int quantity;

    private double price;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public CartItem(ProductDTO dto, Cart cart, double size){
        this.cart = cart;
        this.productId = dto.id();
        this.name = dto.name();
        this.brand = dto.brand();
        this.size = size;
        this.imageUrl = dto.url();
        this.quantity = 1;
        this.price = dto.price();
    }
}

