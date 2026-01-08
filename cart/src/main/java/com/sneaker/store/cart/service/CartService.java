package com.sneaker.store.cart.service;


import com.sneaker.store.cart.dto.CartDTO;

public interface CartService {
    Long createCart();

    void addToCart(Long cartId, Long productId);

    void deleteFromCart(Long cartId, Long productId);

    void increaseQuantity(Long cartId, Long itemId);

    void decreaseQuantity(Long cartId, Long itemId);

    CartDTO getCart(Long cardId);
}
