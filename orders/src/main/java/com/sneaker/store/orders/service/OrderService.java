package com.sneaker.store.orders.service;

import com.sneaker.store.orders.dto.OrderCreateDTO;
import com.sneaker.store.orders.dto.OrderDTO;
import com.sneaker.store.orders.dto.OrderItemInProfile;
import com.sneaker.store.orders.model.Order;

import java.util.List;

public interface OrderService {
    String createOrder(OrderCreateDTO dto, String email);

    void saveOrder(Order order);

    OrderDTO getOrder(String orderNumber);

    OrderDTO cancelOrder(Long orderId);


    List<OrderItemInProfile> getALlOrdersForCustomer(String name);
}
