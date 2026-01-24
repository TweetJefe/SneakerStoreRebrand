package com.sneaker.store.orders.service;

import com.sneaker.store.orders.dto.OrderCreateDTO;
import com.sneaker.store.orders.dto.OrderDTO;
import com.sneaker.store.orders.dto.OrderItemInProfile;
import com.sneaker.store.orders.dto.ProductItemDTO;
import com.sneaker.store.orders.enums.DeliveryMethod;
import com.sneaker.store.orders.enums.PaymentMethod;
import com.sneaker.store.orders.model.Order;
import com.sneaker.store.orders.model.ProductItem;

public class OrderMapper {
    public static Order mapToEntity(OrderCreateDTO dto, String email){
        Order order = new Order();
        order.setFirstName(dto.firstName());
        order.setLastName(dto.lastName());
        order.setEmail(email);
        order.setPhone(dto.phone());
        order.setAddress(dto.address());
        order.getItems().addAll(dto.items()
                .stream()
                .map(item -> {
                    var p = mapToItem(item);
                    p.setOrder(order);
                    return p;
                }).toList()
        );
        order.setDeliveryMethod(DeliveryMethod.valueOf(dto.delivery()));
        order.setPaymentMethod(dto.payment() ? PaymentMethod.ONLINE : PaymentMethod.UPON_RECEIPT);
        return order;
    }

    public static OrderItemInProfile mapToItemInProfile(Order order){
        return new OrderItemInProfile(
                order.getOrderNumber(),
                order.getItems().stream().map(productItem -> productItem.getPrice() * productItem.getQuantity()).reduce(0.0, Double::sum),
                order.getOrderDate().toLocalDate(),
                order.getOrderStatus().toString()
        );
    }

    public static OrderDTO mapToDTO(Order order){
        return OrderDTOBuilder.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .firstName(order.getFirstName())
                .lastName(order.getLastName())
                .orderDate(order.getOrderDate())
                .email(order.getEmail())
                .phone(order.getPhone())
                .address(order.getAddress())
                .orderStatus(order.getOrderStatus())
                .items(order.getItems())
                .delivery(order.getDeliveryMethod())
                .payment(order.getPaymentMethod())
                .build();
    }

    public static ProductItem mapToItem(ProductItemDTO dto){
        return new ProductItem(
                dto.productId(),
                dto.name(),
                dto.brand(),
                dto.imageUrl(),
                dto.quantity(),
                dto.size(),
                dto.price()
        );
    }

    public static ProductItemDTO mapToDTO(ProductItem item){
        return new ProductItemDTO(
                0L,
                item.getProductId(),
                item.getName(),
                item.getBrand(),
                item.getSize(),
                item.getImageUrl(),
                item.getQuantity(),
                item.getPrice()
        );
    }
}
