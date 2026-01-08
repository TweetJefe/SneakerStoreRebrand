package com.sneaker.store.shipments.controller;

import com.sneaker.store.shipments.dto.GetShipmentDTO;
import com.sneaker.store.shipments.dto.ShipmentDTO;
import com.sneaker.store.shipments.enums.Status;
import com.sneaker.store.shipments.service.ShipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "Shipments", description = "API for work with shipments")
@RestController
@RequestMapping("/api/shipments")
@RequiredArgsConstructor

public class ShipmentController {
    private final ShipmentService service;

    @Operation(
            summary = "Create a shipment",
            tags = {"Shipments"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Shipment was created"),
            @ApiResponse(responseCode = "401", description = "Unathorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/create")
    public ResponseEntity<Void> createShipment(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data for creating shipment",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ShipmentDTO.class)
                    )
            )
            @RequestBody ShipmentDTO dto
    ) {
        service.createShipment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Update shipment status",
            tags = {"Shipments"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Shipment status was updated"),
            @ApiResponse(responseCode = "401", description = "Unathorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{orderNumber}/status")
    public ResponseEntity<String> updateShipmentStatus(
            @Parameter(description = "Order Number", example = "ORD-1231241241")
            @PathVariable String orderNumber,
            @Parameter(description = "Status of order", example = "ACCEPTED")
            @RequestParam Status status
    ) {
        service.updateStatus(orderNumber, status);
        String message = String.format(
                "Статус заказа %s обновлён на %s в %s",
                orderNumber,
                status,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @Operation(
            summary = "Show shipment by order",
            tags = {"Shipments"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "401", description = "Unathorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/order/{orderNumber}")
    public ResponseEntity<GetShipmentDTO> getShipmentByOrder(
            @Parameter(description = "Order Number", example = "ORD-1231241241")
            @PathVariable String orderNumber
    ) {
        service.getShipmentByOrderNumber(orderNumber);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
