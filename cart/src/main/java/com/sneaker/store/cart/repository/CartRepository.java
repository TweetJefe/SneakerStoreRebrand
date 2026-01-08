package com.sneaker.store.cart.repository;

import com.sneaker.store.cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
