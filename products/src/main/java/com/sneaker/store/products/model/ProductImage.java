package com.sneaker.store.products.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_image")
@NoArgsConstructor
@Getter
@Setter
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private String url;

    public ProductImage(Long productId, String url) {
        this.productId = productId;
        this.url = url;
    }
}
