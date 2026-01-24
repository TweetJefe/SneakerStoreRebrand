package com.sneaker.store.cart.service;


import com.sneaker.store.cart.dto.CartDTO;
import com.sneaker.store.cart.dto.ProductDTO;

public interface CartService {
    Long createCart();

    void addToCart(Long cartId, ProductDTO dto, Double size);

    void deleteFromCart(Long cartId, Long productId, Double size);

    void increaseQuantity(Long cartId, Long itemId, Double size);

    void decreaseQuantity(Long cartId, Long itemId, Double size);

    CartDTO getCart(Long cardId);
}
