package com.sneaker.store.products.service;

import com.sneaker.store.products.dto.ProductCriteria;
import com.sneaker.store.products.dto.ProductDTO;
import com.sneaker.store.products.dto.RRRCProductDTO;
import com.sneaker.store.products.dto.UpdateProductDTO;
import com.sneaker.store.products.model.Product;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ProductService {

    CompletableFuture<Page<ProductDTO>> findByCriteria(ProductCriteria criteria, Pageable pageable);

    void createProduct(ProductDTO dto);

    Product saveProduct(Product product);

    void updateProduct(UpdateProductDTO dto);

    void deleteProduct(Long publicId);

    void reserveProduct(RRRCProductDTO dto);

    void releaseProduct(RRRCProductDTO dto);

    List<String> getAllBrands();

    ProductDTO getProductById(Long id);

    Page<ProductDTO> getAllWithoutCriteria(Pageable pageable);

    List<Double> getAllSizes();

    List<ProductDTO> findRecommendByPrice(Long id);

    List<ProductDTO> findRecommendByBrand(Long id);
}
