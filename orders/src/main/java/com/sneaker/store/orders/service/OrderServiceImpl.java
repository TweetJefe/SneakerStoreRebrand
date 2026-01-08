package com.sneaker.store.orders.service;

import com.sneaker.store.orders.dto.OrderCreateDTO;
import com.sneaker.store.orders.dto.OrderDTO;
import com.sneaker.store.orders.enums.OrderStatus;
import com.sneaker.store.orders.exceptions.NullableViolation;
import com.sneaker.store.orders.exceptions.ServerException;
import com.sneaker.store.orders.exceptions.UniquenessViolation;
import com.sneaker.store.orders.mapper.OrderMapper;
import com.sneaker.store.orders.model.Order;
import com.sneaker.store.orders.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    private final String PostgreSQLUniquenessViolation = "23505";
    private final String PostgreSQLNullableViolation = "23502";

    @Override
    public String createOrder(OrderCreateDTO dto){
        Order order = orderMapper.toEntityCreate(dto);

        order.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 16).toUpperCase());
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.NEW);

        saveOrder(order);
        return order.getOrderNumber();
    }


    @Override
    public void saveOrder(Order order) {
        try{
            orderRepository.save(order);
        }catch (DataIntegrityViolationException exception){
            Throwable cause = exception.getCause();
            if(cause instanceof ConstraintViolationException cve){
                String sqlState = cve.getSQLState();
                if(sqlState.equals(PostgreSQLUniquenessViolation)){
                    String constraintName = cve.getConstraintName();
                    throw new UniquenessViolation(constraintName);
                }else if(sqlState.equals(PostgreSQLNullableViolation)){
                    throw new NullableViolation();
                }
            }else{
                throw new ServerException();
            }
        }
    }


    @Transactional(readOnly = true)
    @Override
    public OrderDTO getOrder(Long orderId) {
        Order order =  orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        return orderMapper.toDTO(order);
    }

    @Override
    public OrderDTO cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        order.setOrderStatus(OrderStatus.CANCELLED);
        saveOrder(order);
        return orderMapper.toDTO(order);
    }

    @Transactional (readOnly = true)
    @Override
    public Page<OrderDTO> getOrdersByCustomer(Long customerId, Pageable pageable){
        return orderRepository.findAllByCustomerId(customerId, pageable).map(orderMapper::toDTO);
    }
}