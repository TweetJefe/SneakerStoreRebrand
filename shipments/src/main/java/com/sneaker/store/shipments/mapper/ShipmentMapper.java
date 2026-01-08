package com.sneaker.store.shipments.mapper;

import com.sneaker.store.shipments.dto.GetShipmentDTO;
import com.sneaker.store.shipments.dto.ShipmentDTO;
import com.sneaker.store.shipments.dto.UpdateShipmentDTO;
import com.sneaker.store.shipments.model.Shipment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ShipmentMapper{

    ShipmentMapper INSTANCE = Mappers.getMapper(ShipmentMapper.class);

    ShipmentDTO toDTO (Shipment shipment);
    Shipment toEntity (ShipmentDTO shipmentDTO);

    GetShipmentDTO toGetDTO (Shipment shipment);
    UpdateShipmentDTO toUpdateDTO (Shipment shipment);
}

