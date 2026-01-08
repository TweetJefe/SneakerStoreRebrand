package com.sneaker.store.cart.service;

import com.sneaker.store.cart.dto.CartDTO;
import com.sneaker.store.cart.dto.CartItemDTO;
import com.sneaker.store.cart.model.Cart;
import com.sneaker.store.cart.model.CartItem;
import com.sneaker.store.cart.repository.CartRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository repository;

    @Override
    public Long createCart() {
        Cart cart = new Cart();
        return repository.save(cart).getCartId();
    }

    @Override
    public void addToCart(Long cartId, Long productId) {
        Cart cart = repository.findById(cartId).orElseThrow(EntityNotFoundException::new);
        CartItem item = new CartItem(productId, cart);
        cart.getProductList().add(item);
        cart.setPrice(item.getPrice() + cart.getPrice());
        repository.save(cart);
    }

    @Override
    public void deleteFromCart(Long cartId, Long productId) {
        Cart cart = repository.findById(cartId)
                .orElseThrow(EntityNotFoundException::new);

        cart.getProductList().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst().ifPresent(cartItem -> {
                    cart.getProductList().remove(cartItem);
                    cart.setPrice(cart.getPrice() - cartItem.getPrice());
                });

        repository.save(cart);
    }

    @Override
    public void increaseQuantity(Long cartId, Long itemId) {
        Cart cart =  repository.findById(cartId)
                .orElseThrow(EntityNotFoundException::new);
        cart.getProductList().stream()
                .filter(p -> p.getId().equals(itemId))
                .findFirst()
                .ifPresent(p -> {
                    p.setQuantity(p.getQuantity() + 1);
                    cart.setPrice(cart.getPrice() + p.getPrice());
                });
        repository.save(cart);
    }

    @Override
    public void decreaseQuantity(Long cartId, Long itemId) {
        Cart cart = repository.findById(cartId)
                .orElseThrow(EntityNotFoundException::new);

        cart.getProductList().stream()
                .filter(p -> p.getId().equals(itemId))
                .findFirst()
                .ifPresent(p -> {
                    if(p.getQuantity() == 1) {
                        deleteFromCart(cartId, itemId);
                    }else{
                        p.setQuantity(p.getQuantity() - 1);
                        cart.setPrice(cart.getPrice() - p.getPrice());
                    }});
        repository.save(cart);
    }

    @Override
    public CartDTO getCart(Long cartId) {
        return repository.findById(cartId).map(cart -> new CartDTO(
                cart.getCartId(),
                cart.getPrice(),
                cart.getProductList().stream().map(item -> new CartItemDTO(
                        item.getId(), item.getProductName(), item.getQuantity(), item.getPrice()
                )).toList()
        )).orElseThrow(EntityNotFoundException::new);
    }
}
