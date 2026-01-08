package com.sneaker.store.orders.service;

import com.sneaker.store.orders.dto.OrderCreateDTO;
import com.sneaker.store.orders.dto.OrderDTO;
import com.sneaker.store.orders.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    String createOrder(OrderCreateDTO dto);

    void saveOrder(Order order);

    OrderDTO getOrder(Long orderId);

    OrderDTO cancelOrder(Long orderId);

    Page<OrderDTO> getOrdersByCustomer(Long customerId, Pageable pageable);
}
