package com.sneaker.store.products.service;

import com.sneaker.store.products.dto.ProductCriteria;
import com.sneaker.store.products.dto.ProductDTO;
import com.sneaker.store.products.dto.RRRCProductDTO;
import com.sneaker.store.products.dto.UpdateProductDTO;
import com.sneaker.store.products.enums.Category;
import com.sneaker.store.products.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;

public interface ProductService {

    CompletableFuture<Page<ProductDTO>> findByCriteria(ProductCriteria criteria, Pageable pageable);

    void createProduct(ProductDTO dto);

    void saveProduct(Product product);

    void updateProduct(UpdateProductDTO dto);

    void deleteProduct(Long publicId);

    CompletableFuture<Page<ProductDTO>> getProductByBrand(String brand, Pageable pageable);

    CompletableFuture<Page<ProductDTO>> getProductByPrice(double price, Pageable pageable);

    CompletableFuture<Page<ProductDTO>> getProductByCategory(Category category, Pageable pageable);

    CompletableFuture<Page<ProductDTO>> findProductByName(String name, Pageable pageable);

    void reserveProduct(RRRCProductDTO dto);

    void releaseProduct(RRRCProductDTO dto);
}
