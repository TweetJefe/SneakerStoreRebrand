package com.sneaker.store.cart.service;

import com.sneaker.store.cart.dto.CartDTO;
import com.sneaker.store.cart.dto.CartItemDTO;
import com.sneaker.store.cart.dto.ProductDTO;
import com.sneaker.store.cart.model.Cart;
import com.sneaker.store.cart.model.CartItem;
import com.sneaker.store.cart.repository.CartRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;

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
    public void addToCart(Long cartId, ProductDTO dto, Double size) {
        Cart cart = repository.findById(cartId).orElseThrow(EntityNotFoundException::new);
        var item = cart.getProductList().stream()
                .filter(product -> product.getProductId().equals(dto.id()) && product.getSize().equals(size))
                .findFirst();
        if(item.isPresent()){
            increaseQuantity(cartId, dto.id(), size);
        }else {
            CartItem cartItem = new CartItem(dto, cart, size);
            cart.getProductList().add(cartItem);
            cart.setPrice(cartItem.getPrice() + cart.getPrice());
            repository.save(cart);
        }
    }

    @Override
    public void deleteFromCart(Long cartId, Long productId, Double size) {
        Cart cart = repository.findById(cartId)
                .orElseThrow(EntityNotFoundException::new);

        cart.getProductList().stream()
                .filter(product -> product.getProductId().equals(productId) && product.getSize().equals(size))
                .findFirst()
                .ifPresent(product -> {
                    cart.getProductList().remove(product);
                    repository.save(cart);
                }
        );

    }

    @Override
    public void increaseQuantity(Long cartId, Long productId, Double size) {
        Cart cart =  repository.findById(cartId)
                .orElseThrow(EntityNotFoundException::new);
        cart.getProductList().stream()
                .filter(p -> p.getProductId().equals(productId) && p.getSize().equals(size))
                .findFirst()
                .ifPresent(p -> {
                    if(p.getQuantity() + 1 > 10){
                        throw new IllegalArgumentException("");
                    }
                    p.setQuantity(p.getQuantity() + 1);
                    cart.setPrice(cart.getPrice() + p.getPrice());
                });
        repository.save(cart);
    }

    @Override
    public void decreaseQuantity(Long cartId, Long productId, Double size) {
        Cart cart = repository.findById(cartId)
                .orElseThrow(EntityNotFoundException::new);

        cart.getProductList().stream()
                .filter(p -> p.getProductId().equals(productId) && p.getSize().equals(size))
                .findFirst()
                .ifPresent(p -> {
                    if(p.getQuantity() == 1) {
                        deleteFromCart(cartId, productId, size);
                    }else{
                        p.setQuantity(p.getQuantity() - 1);
                        cart.setPrice(cart.getPrice() - p.getPrice());
                    }});
        repository.save(cart);
    }

    @Override
    public CartDTO getCart(Long cartId) {
        return repository.findById(cartId).map(cart -> new CartDTO(
                cart.getPrice(),
                cart.getProductList().stream()
                        .sorted(Comparator.comparingLong(CartItem::getProductId))
                        .map(cartItem -> new CartItemDTO(
                        cartItem.getId(),
                        cartItem.getProductId(),
                        cartItem.getName(),
                        cartItem.getBrand(),
                        cartItem.getSize(),
                        cartItem.getImageUrl(),
                        cartItem.getQuantity(),
                        cartItem.getPrice()
                )).toList()
        )).orElseThrow(EntityNotFoundException::new);
    }
}
