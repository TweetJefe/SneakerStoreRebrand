package com.sneaker.store.products.service;


import com.sneaker.store.products.dto.ProductDTO;
import com.sneaker.store.products.dto.UpdateProductDTO;
import com.sneaker.store.products.enums.Category;
import com.sneaker.store.products.mapper.ProductMapper;
import com.sneaker.store.products.model.Product;
import com.sneaker.store.products.model.ProductImage;
import com.sneaker.store.products.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceUnitTest {
    @Mock
    private ProductRepository repository;
    @Spy
    private ProductMapper mapper;
    @InjectMocks
    private ProductServiceImpl service;


    @Nested
    class updateProductTests{

        private Product product;
        private UpdateProductDTO expectedDTO;

        @BeforeEach
        void setUp() {
            product = new Product();
            product.setId(1L);
            product.setBrand("Nike");
            product.setModel("Air Force 1");
            product.setPrice(100);
            product.setArticle("1234567890");
            product.setCategory(Category.MALE);
            product.setName(product.getBrand() + " " + product.getModel());

            expectedDTO = new UpdateProductDTO(
                    1L,
                    "Nike",
                    "Nike Air Force 1",
                    "Air Force 1",
                    Category.MALE,
                    "1234567890",
                    45,
                    100,
                    new ProductImage());
            when(repository.findById(expectedDTO.id())).thenReturn(Optional.of(product));
            doNothing().when(repository.save(product));
            doNothing().when(service).saveProduct(product);
        }
        @AfterEach
        void verification() {
            verify(repository).findById(expectedDTO.id());
        }
        @Test
        void updateProduct_UpdateProductDTO_ShouldUpdateProductWithId() {
            service.updateProduct(expectedDTO);

            assertEquals(expectedDTO.brand(), product.getBrand());
            assertEquals(expectedDTO.model(), product.getModel());
            assertEquals(expectedDTO.price(), product.getPrice());
            assertEquals(expectedDTO.article(), product.getArticle());
            assertEquals(expectedDTO.category(), product.getCategory());
            assertEquals(expectedDTO.name(), product.getName());
            assertEquals(expectedDTO.image(), product.getImage());

            verify(repository).save(product);
            verify(service).saveProduct(product);
        }

        @Test
        void updateProduct_Should_Throw_Entity_Not_Found(){
            when(repository.findById(expectedDTO.id())).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> service.updateProduct(expectedDTO));
            verify(service, never()).saveProduct(any());
            verify(repository, never()).save(any());
        }
    }

    @Nested
    class createProductTests {
        private Product product;
        private ProductDTO expectedDTO;
        private ProductDTO testDTO;

        @BeforeEach
        void setUp() {
            product = new Product();
            product.setId(1L);
            product.setBrand("Nike");
            product.setModel("Air Force 1");
            product.setPrice(100);
            product.setArticle("1234567890");
            product.setCategory(Category.MALE);
            product.setName(product.getBrand() + " " + product.getModel());

            expectedDTO = new ProductDTO(
                    1L,
                    "Nike",
                    "Nike Air Force 1",
                    "Air Force 1",
                    Category.MALE,
                    "1234567890",
                    45,
                    100,
                    new ProductImage());
            when(mapper.toEntity(expectedDTO)).thenReturn(product);
            doNothing().when(repository).save(product);
            doNothing().when(service).saveProduct(product);
        }
        @AfterEach
        void verification() {
            verify(mapper).toEntity(expectedDTO);
        }

        @Test
        void createProduct_ProductDTO_Should_CreateProductWithId() {
            service.createProduct(expectedDTO);

            assertEquals(expectedDTO.brand(), product.getBrand());
            assertEquals(expectedDTO.model(), product.getModel());
            assertEquals(expectedDTO.price(), product.getPrice());
            assertEquals(expectedDTO.article(), product.getArticle());
            assertEquals(expectedDTO.category(), product.getCategory());
            assertEquals(expectedDTO.name(), product.getName());
            assertEquals(expectedDTO.image(), product.getImage());

            verify(repository).save(product);
            verify(service).saveProduct(product);
        }
    }

    @Nested
    class deleteProductTests {
        private Product product;
        private ProductDTO expectedDTO;

        @BeforeEach
        void setUp() {
            product = new Product();
            product.setId(1L);
            product.setBrand("Nike");
            product.setModel("Air Force 1");
            product.setPrice(100);
            product.setArticle("1234567890");
            product.setCategory(Category.MALE);
            product.setName(product.getBrand() + " " + product.getModel());

            expectedDTO = new ProductDTO(
                    1L,
                    "Nike",
                    "Nike Air Force 1",
                    "Air Force 1",
                    Category.MALE,
                    "1234567890",
                    45,
                    100,
                    new ProductImage());
            when(repository.findById(expectedDTO.id())).thenReturn(Optional.empty());
        }
        @Test
        void deleteProduct_WithInvalidData_ShouldThrowEntityNotFound(){
            assertThrows(EntityNotFoundException.class, () -> service.deleteProduct(expectedDTO.id()));
            verify(repository).findById(expectedDTO.id());
        }
    }

    @Nested
    class getProductByBrandTests{
        private Product product;
        private ProductDTO expectedDTO;
        private Pageable pageable;
        private Page<Product> productPage;

        @BeforeEach
        void setUp() {
            pageable = PageRequest.of(0, 10);

            product = new Product();
            product.setId(1L);
            product.setBrand("Nike");
            product.setModel("Air Force 1");
            product.setPrice(100);
            product.setArticle("1234567890");
            product.setCategory(Category.MALE);
            product.setName(product.getBrand() + " " + product.getModel());

            productPage =
                    new PageImpl<>(List.of(product), pageable, 1);

            expectedDTO = new ProductDTO(
                    1L,
                    "Nike",
                    "Nike Air Force 1",
                    "Air Force 1",
                    Category.MALE,
                    "1234567890",
                    45,
                    100,
                    new ProductImage());
            Page<ProductDTO> productDTOPage =
                    new PageImpl<>(List.of(expectedDTO), pageable, 1);
            when(repository.findAllByBrand(expectedDTO.brand(), pageable)).thenReturn(productPage);
            when(mapper.toPageResponseDTO(productPage)).thenReturn(productDTOPage);
        }

        @Test
        void getProductByBrand_WithValidData_ShouldReturnPage(){
            CompletableFuture<Page<ProductDTO>> future = service.getProductByBrand(expectedDTO.brand(), pageable);

            Page<ProductDTO>  result = future.join(); //ждем пока код из сервиса доработает

            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            assertFalse(result.getContent().isEmpty());

            ProductDTO dto  = result.getContent().get(0);
            assertEquals(expectedDTO.id(), dto.id());
            assertEquals(expectedDTO.brand(), dto.brand());
            assertEquals(expectedDTO.model(), dto.model());
            assertEquals(expectedDTO.price(), dto.price());
            assertEquals(expectedDTO.article(), dto.article());
            assertEquals(expectedDTO.category(), dto.category());
            assertEquals(expectedDTO.name(), dto.name());
            assertEquals(expectedDTO.image(), dto.image());

            verify(repository.findAllByBrand(expectedDTO.brand(), pageable));
            verify(mapper.toPageResponseDTO(productPage));
        }
    }
}
