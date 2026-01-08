package com.sneaker.store.shipments.service;


import com.sneaker.store.shipments.dto.GetShipmentDTO;
import com.sneaker.store.shipments.dto.UpdateShipmentDTO;
import com.sneaker.store.shipments.enums.Status;
import com.sneaker.store.shipments.mapper.ShipmentMapper;
import com.sneaker.store.shipments.model.Shipment;
import com.sneaker.store.shipments.repository.ShipmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShipmentServiceUnitTest {
    @Mock
    private ShipmentRepository repository;

    @Mock
    private ShipmentMapper mapper;

    @InjectMocks
    private ShipmentServiceImpl service;

    private String orderNumber = "ORD-1111";

    @Nested
    class getShipmentByOrderNumberTests{

        private Shipment shipment;
        private GetShipmentDTO expectedDTO;
        @BeforeEach
        public void setUp() {

            Shipment shipment = new Shipment();
            shipment.setOrderNumber(orderNumber);
            shipment.setItems(List.of("Air Force 1 white", "Air Jordan 1 Bloodline"));
            shipment.setQuantity(2);

            GetShipmentDTO expectedDTO = new GetShipmentDTO(
                    orderNumber,
                    List.of("Air Force 1 white", "Air Jordan 1 Bloodline"),
                    2
            );
            when(repository.findByOrderNumber(orderNumber)).thenReturn(Optional.of(shipment));
            when(mapper.toGetDTO(shipment)).thenReturn(expectedDTO);
        }

        @AfterEach
        void verification() {
            verify(repository).findByOrderNumber(orderNumber);

        }

        @Test
        void getShipmentByOrderNumber_ShouldReturnDTO(){
            GetShipmentDTO actualDTO = service.getShipmentByOrderNumber(orderNumber);

            assertNotNull(actualDTO);
            assertEquals(expectedDTO, actualDTO);
            verify(mapper).toGetDTO(shipment);
        }

        @Test
        void getShipmentByOrderNumber_throwsEntityNotFoundException(){
            when(repository.findByOrderNumber(orderNumber)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () -> service.getShipmentByOrderNumber(orderNumber));
            verify(mapper, never()).toGetDTO(any());
        }
    }

    @Nested
    class updateShipmentByOrderNumberTests{
        Status status = Status.DELIVERED;
        private GetShipmentDTO expectedDTO;
        @BeforeEach
        void SetUp() {
            Shipment shipment = new Shipment();
            shipment.setOrderNumber(orderNumber);
            shipment.setStatus(Status.PENDING);
            shipment.setUpdatedAt(LocalDateTime.now());

            UpdateShipmentDTO expectedDTO = new UpdateShipmentDTO(Status.DELIVERED, orderNumber, LocalDateTime.now());

            when(repository.findByOrderNumber(orderNumber)).thenReturn(Optional.of(shipment));
            when(mapper.toUpdateDTO(shipment)).thenReturn(expectedDTO);
        }

        @AfterEach
        void verification() {
            verify(repository).findByOrderNumber(orderNumber);
        }

        @Test
        void updateShipmentByOrderNumber_ShouldReturnDTO(){
            UpdateShipmentDTO testDTO = service.updateStatus(orderNumber, status);

            assertNotNull(testDTO);
            assertEquals(expectedDTO, testDTO);
        }

        @Test
        void updateShipmentByOrderNumber_throwsEntityNotFoundException(){
            when(repository.findByOrderNumber(orderNumber)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> service.updateStatus(orderNumber, status));
            verify(mapper, never()).toGetDTO(any());
        }
    }
}
