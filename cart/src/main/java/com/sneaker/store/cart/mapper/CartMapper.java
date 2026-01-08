package com.sneaker.store.cart.mapper;

import com.sneaker.store.cart.dto.CartDTO;
import com.sneaker.store.cart.model.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CartMapper{

    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    CartDTO toDTO (Cart cart);
    Cart toEntity (CartDTO cartDTO);

}
