package com.sneaker.store.shipments.service;

import com.sneaker.store.shipments.dto.GetShipmentDTO;
import com.sneaker.store.shipments.dto.ShipmentDTO;
import com.sneaker.store.shipments.dto.UpdateShipmentDTO;
import com.sneaker.store.shipments.enums.Status;
import com.sneaker.store.shipments.model.Shipment;

public interface ShipmentService{
    void createShipment(ShipmentDTO dto);

    void saveShipment(Shipment shipment);

    GetShipmentDTO getShipmentByOrderNumber(String orderNumber);

    UpdateShipmentDTO updateStatus(String orderNumber, Status status);
}

