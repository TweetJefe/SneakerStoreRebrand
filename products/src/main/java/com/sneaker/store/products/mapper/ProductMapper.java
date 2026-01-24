package com.sneaker.store.products.mapper;

import com.sneaker.store.products.dto.ProductDTO;
import com.sneaker.store.products.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface ProductMapper{

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
    Product toEntity (ProductDTO productDTO);

    default Page<ProductDTO> toPageResponseDTO(Page<Product> productPage) {
        return productPage.map(this::toDto);
    }
    ProductDTO toDto(Product product);
}
