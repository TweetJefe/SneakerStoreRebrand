package com.sneaker.store.products.model;

import com.sneaker.store.products.enums.Category;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String brand;
    private String name;
    private String model;
    private Category category;
    private String article;
    private double size;

    private double price;

    @ManyToOne
    @JoinColumn(name = "image_id")
    private ProductImage image;


    private int avaibleQuantity;
}
