package com.sneaker.store.products.service;

import com.sneaker.store.products.dto.ProductCriteria;
import com.sneaker.store.products.dto.ProductDTO;
import com.sneaker.store.products.dto.RRRCProductDTO;
import com.sneaker.store.products.dto.UpdateProductDTO;
import com.sneaker.store.products.enums.Category;
import com.sneaker.store.products.exceptions.NullableViolation;
import com.sneaker.store.products.exceptions.ServerException;
import com.sneaker.store.products.exceptions.UniquenessViolation;
import com.sneaker.store.products.mapper.ProductMapper;
import com.sneaker.store.products.model.Product;
import com.sneaker.store.products.repository.ProductFinder;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import com.sneaker.store.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final ProductFinder finder;

    private final String PostgreSQLUniquenessViolation = "23505";
    private final String PostgreSQLNullableViolation = "23502";

    @Async
    @Override
    public CompletableFuture<Page<ProductDTO>> findByCriteria(ProductCriteria criteria, Pageable pageable) {
        return finder.findByCriteria(criteria, pageable);
    }

    @Override
    public void createProduct(ProductDTO dto) {
        Product product = mapper.toEntity(dto);

        product.setBrand(dto.brand());
        product.setName(dto.brand() + " " + dto.model());
        product.setModel(dto.model());
        product.setCategory(dto.category());
        product.setSize(dto.size());
        product.setPrice(dto.price());
        product.setImage(dto.image());

        saveProduct(product);
    }

    @Override
    public void saveProduct(Product product) {
        try{
            repository.save(product);
        }catch (DataIntegrityViolationException exception){
            Throwable cause = exception.getCause();
            if(cause instanceof ConstraintViolationException cve){
                String sqlState =cve.getSQLState();
                if(sqlState.equals(PostgreSQLUniquenessViolation)){
                    String constraintName =cve.getConstraintName();
                    throw new UniquenessViolation(constraintName);
                }else if(sqlState.equals(PostgreSQLNullableViolation)){
                    throw new NullableViolation();
                }
            }else{
                throw new ServerException();
            }
        }
    }

    @Override
    public void updateProduct(UpdateProductDTO dto) {
        repository.findById(dto.id()).ifPresentOrElse(
                product -> {
                    product.setName(dto.name());
                    product.setModel(dto.model());
                    product.setCategory(dto.category());
                    product.setSize(dto.size());
                    product.setPrice(dto.price());
                    product.setImage(dto.image());
                    saveProduct(product);
                },
                () -> {
                    throw new EntityNotFoundException("");
                }
        );
    }

    @Override
    public void deleteProduct(Long Id) {
        repository.findById(Id).ifPresentOrElse(
            product -> repository.deleteById(Id),
            () -> {
                throw new EntityNotFoundException("");
            }
        );
    }

    @Async
    @Override
    public CompletableFuture<Page<ProductDTO>> getProductByBrand(String brand, Pageable pageable) {
        return CompletableFuture.supplyAsync(() -> {
            Page<Product> page =  repository.findAllByBrand(brand, pageable);
            return mapper.toPageResponseDTO(page);
        });
    }

    @Async
    @Override
    public CompletableFuture<Page<ProductDTO>> getProductByPrice(double price, Pageable pageable) {
        return CompletableFuture.supplyAsync(() -> {
            Page<Product> page = repository.findAllByPrice(price, pageable);
            return mapper.toPageResponseDTO(page);
        });
    }

    @Async
    @Override
    public CompletableFuture<Page<ProductDTO>> getProductByCategory(Category category, Pageable pageable) {
        return CompletableFuture.supplyAsync(() -> {
            Page<Product> page = repository.findAllByCategory(category, pageable);
            return mapper.toPageResponseDTO(page);
        });
    }

    @Async
    @Override
    public CompletableFuture<Page<ProductDTO>> findProductByName(String name, Pageable pageable) {
        return CompletableFuture.supplyAsync(() ->{
            Page<Product> page = repository.findAllByNameContainingIgnoreCase(name, pageable);
            return mapper.toPageResponseDTO(page);
        });
    }

    @Override
    public void reserveProduct(RRRCProductDTO dto) {
        repository.findById(dto.id()).ifPresentOrElse(
                product -> {
                    if (product.getAvaibleQuantity() < dto.quantity()){
                        throw new IllegalStateException("Quantity is a bit higher than available");
                    }
                    product.setAvaibleQuantity(product.getAvaibleQuantity() - dto.quantity());
                    saveProduct(product);
                },
                () -> {
                    throw new EntityNotFoundException("");
                }
        );
    }

    @Override
    public void releaseProduct(RRRCProductDTO dto) {
        repository.findById(dto.id()).ifPresentOrElse(
                product -> {
                    product.setAvaibleQuantity(product.getAvaibleQuantity() + dto.quantity());
                    saveProduct(product);
                },
                () -> {
                    throw new EntityNotFoundException("");
                }
        );
    }
}
