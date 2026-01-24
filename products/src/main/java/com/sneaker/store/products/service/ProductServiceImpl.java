package com.sneaker.store.products.service;

import com.sneaker.store.products.dto.ProductCriteria;
import com.sneaker.store.products.dto.ProductDTO;
import com.sneaker.store.products.dto.RRRCProductDTO;
import com.sneaker.store.products.dto.UpdateProductDTO;
import com.sneaker.store.products.exceptions.NullableViolation;
import com.sneaker.store.products.exceptions.ServerException;
import com.sneaker.store.products.exceptions.UniquenessViolation;
import com.sneaker.store.products.mapper.ProductMapper;
import com.sneaker.store.products.model.Product;
import com.sneaker.store.products.model.ProductImage;
import com.sneaker.store.products.repository.ProductFinder;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import com.sneaker.store.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tools.jackson.databind.cfg.MapperBuilder;

import java.util.List;
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
        var name = (dto.brand() + " " + dto.model());
        if(repository.existsProductByNameAndArticle(name, dto.article())){
            throw new EntityExistsException("");
        }
        Product product = new Product(
                dto.brand(),
                name,
                dto.model(),
                dto.category(),
                dto.article(),
                dto.sizes(),
                dto.price()
        );
        var savedProduct = saveProduct(product);
        System.out.println("Saved product " + savedProduct.getId());
        if(savedProduct != null) {
            ProductImage image = new ProductImage(savedProduct.getId(), dto.url());
            savedProduct.setImage(image);
            saveProduct(savedProduct);
        }
    }

    @Override
    public Product saveProduct(Product product) {
        try{
            return repository.save(product);
        }catch (DataIntegrityViolationException exception){
            Throwable cause = exception.getCause();
            if(cause instanceof ConstraintViolationException cve){
                String sqlState = cve.getSQLState();
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
        return null;
    }

    @Override
    public void updateProduct(UpdateProductDTO dto) {
        repository.findById(dto.id()).ifPresentOrElse(
                product -> {
                    product.setName(dto.name());
                    product.setModel(dto.model());
                    product.setCategory(dto.category());
                    product.setSizes(dto.size());
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

    @Override
    public void reserveProduct(RRRCProductDTO dto) {
        repository.findById(dto.id()).ifPresentOrElse(
                product -> {
                    if (product.getAvailable_quantity() < dto.quantity()){
                        throw new IllegalStateException("Quantity is a bit higher than available");
                    }
                    product.setAvailable_quantity(product.getAvailable_quantity() - dto.quantity());
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
                    product.setAvailable_quantity(product.getAvailable_quantity() + dto.quantity());
                    saveProduct(product);
                },
                () -> {
                    throw new EntityNotFoundException("");
                }
        );
    }

    @Override
    public List<String> getAllBrands(){
        return repository.getAllBrands();
    }

    @Override
    public List<Double> getAllSizes(){
        return repository.findAllSizes();
    }

    @Override
    public List<ProductDTO> findRecommendByPrice(Long id) {
        var product = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        return repository.findRecommendByPrice(product.getPrice())
                .stream()
                .map(ProductServiceImpl::mapToDTO).toList();
    }

    @Override
    public List<ProductDTO> findRecommendByBrand(Long id) {
        var product = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        return repository.findRecommendByBrand(id, product.getBrand(), Limit.of(3))
                .stream()
                .map(ProductServiceImpl::mapToDTO)
                .toList();
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        return ProductServiceImpl.mapToDTO(product);
    }

    @Override
    public Page<ProductDTO> getAllWithoutCriteria(Pageable pageable) {
        return repository.findAll(pageable).map(ProductServiceImpl::mapToDTO);
    }

    private static ProductDTO mapToDTO(Product product){
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getArticle(),
                product.getPrice(),
                product.isCategory(),
                product.getBrand(),
                product.getSizes(),
                product.getModel(),
                product.getImage().getUrl()
        );
    }
}
