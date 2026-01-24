package com.sneaker.store.orders.service;

import com.sneaker.store.orders.dto.OrderDTO;
import com.sneaker.store.orders.enums.DeliveryMethod;
import com.sneaker.store.orders.enums.OrderStatus;
import com.sneaker.store.orders.enums.PaymentMethod;
import com.sneaker.store.orders.model.Address;
import com.sneaker.store.orders.model.ProductItem;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTOBuilder {
    private final OrderDTO order = new OrderDTO();

    private OrderDTOBuilder(){}

    public static OrderDTOBuilder builder(){
        return new OrderDTOBuilder();
    }

    public OrderDTOBuilder id(Long id){
        order.setId(id);
        return this;
    }

    public OrderDTOBuilder orderNumber(String number){
        order.setOrderNumber(number);
        return this;
    }

    public OrderDTOBuilder firstName(String firstName){
        order.setFirstName(firstName);
        return this;
    }

    public OrderDTOBuilder lastName(String lastName){
        order.setLastName(lastName);
        return this;
    }

    public OrderDTOBuilder orderDate(LocalDateTime orderDate){
        order.setOrderDate(orderDate);
        return this;
    }

    public OrderDTOBuilder email(String email){
        order.setEmail(email);
        return this;
    }

    public OrderDTOBuilder phone(String phone){
        order.setPhone(phone);
        return this;
    }

    public OrderDTOBuilder address(Address address){
        order.setAddress(address);
        return this;
    }

    public OrderDTOBuilder orderStatus(OrderStatus orderStatus){
        order.setOrderStatus(orderStatus.toString());
        return this;
    }

    public OrderDTOBuilder items(List<ProductItem> items){
        order.getItems().addAll(items.stream().map(OrderMapper::mapToDTO).toList());
        return this;
    }

    public OrderDTOBuilder delivery(DeliveryMethod method){
        order.setDeliveryMethod(method.getValue());
        return this;
    }

    public OrderDTOBuilder payment(PaymentMethod method){
        order.setPaymentMethod(method.getValue());
        return this;
    }
    public OrderDTO build(){
        return order;
    }
}
