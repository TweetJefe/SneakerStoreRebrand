package com.sneaker.store.shipments.service;

import com.sneaker.store.shipments.dto.GetShipmentDTO;
import com.sneaker.store.shipments.dto.ShipmentDTO;
import com.sneaker.store.shipments.dto.UpdateShipmentDTO;
import com.sneaker.store.shipments.enums.Status;
import com.sneaker.store.shipments.exceptions.NullableViolation;
import com.sneaker.store.shipments.exceptions.ServerException;
import com.sneaker.store.shipments.exceptions.UniquenessViolation;
import com.sneaker.store.shipments.mapper.ShipmentMapper;
import com.sneaker.store.shipments.model.Shipment;
import com.sneaker.store.shipments.repository.ShipmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService{
    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper mapper;

    private final String PostgreSQLUniquenessViolation = "23505";
    private final String PostgreSQLNullableViolation = "23502";


    @Override
    public void createShipment(ShipmentDTO dto){
        Shipment shipment = mapper.toEntity(dto);

        shipment.setStatus(Status.PENDING);
        shipment.setCreatedAt(LocalDateTime.now());
        saveShipment(shipment);
    }

    @Override
    public void saveShipment(Shipment shipment) {
        try{
            shipmentRepository.save(shipment);
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

    @Override
    public GetShipmentDTO getShipmentByOrderNumber(String orderNumber) {
        Shipment shipment = shipmentRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new EntityNotFoundException("Shipment fot the order is not found"));
        return  mapper.toGetDTO(shipment);
    }

    @Override
    public UpdateShipmentDTO updateStatus (String orderNumber, Status status){
        Shipment shipment = shipmentRepository.findByOrderNumber(orderNumber).orElseThrow(EntityNotFoundException::new);

        shipment.setStatus(status);
        shipment.setUpdatedAt(LocalDateTime.now());
        saveShipment(shipment);
        return mapper.toUpdateDTO(shipment);
    }
}
