package com.sneaker.store.orders.repository;

import com.sneaker.store.orders.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByEmail(String email);

    Optional<Order> findByOrderNumber(String orderNumber);
}

