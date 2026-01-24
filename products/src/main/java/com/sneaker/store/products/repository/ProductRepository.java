package com.sneaker.store.products.repository;

import com.sneaker.store.products.model.Product;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select distinct p.brand from Product p")
    List<String> getAllBrands();

    Boolean existsProductByNameAndArticle(String name, String article);

    @Query(value = "select distinct size_id from product_sizes order by size_id asc", nativeQuery = true)
    List<Double> findAllSizes();

    @Query("select distinct p from Product p where p.price >= :price * 0.9 and p.price <= :price * 1.1")
    List<Product> findRecommendByPrice(@Param("price") double price);

    @Query("select distinct p from Product p where p.brand = :brand and p.id != :id")
    List<Product> findRecommendByBrand(@Param("id") Long id, @Param("brand") String brand, Limit limit);
}
