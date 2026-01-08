package com.sneaker.store.shipments.repository;

import com.sneaker.store.shipments.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    Optional<Shipment> findByOrderNumber(String orderNumber);
}

