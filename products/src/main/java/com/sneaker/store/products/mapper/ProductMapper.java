package com.sneaker.store.products.mapper;

import com.sneaker.store.products.dto.ProductDTO;
import com.sneaker.store.products.model.Product;
import com.sneaker.store.products.page.PageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface ProductMapper{

//    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDTO toDTO (Product product);
    Product toEntity (ProductDTO productDTO);

    Page<ProductDTO> toPageResponseDTO (Page<Product> pageResponse);

}
