package com.sneaker.store.products.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @Column(unique = true)
    private String name;

    private String model;
    private boolean category;
    private String article;


    @ElementCollection
    @CollectionTable(name = "product_sizes", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "size_id")
    private List<Double> sizes = new ArrayList<>();

    private double price;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private ProductImage image;

    private int available_quantity;

    public Product(String b, String n, String m, boolean c, String a, List<Double> s, double p){
        this.brand = b;
        this.name = n;
        this.model = m;
        this.category = c;
        this.article = a;
        sizes.addAll(s);
        this.price = p;
    }
}
