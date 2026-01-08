package com.sneaker.store.orders.controller;

import com.sneaker.store.orders.dto.OrderCreateDTO;
import com.sneaker.store.orders.dto.OrderDTO;
import com.sneaker.store.orders.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Orders", description = "API for work with orders")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    @Operation(
            summary = "Create an order",
            tags = {"Orders"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order was created", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = String.class)
            )),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/create")
    public ResponseEntity<String> createOrder(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data for create an order",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderCreateDTO.class)
                    )
            )
            @Valid @RequestBody OrderCreateDTO dto) {
        service.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Get an order",
            tags = {"Orders"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order was returned", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = OrderDTO.class)
            )),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Order was not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}/get")
    public ResponseEntity<OrderDTO> getOrder(
            @Parameter(description = "Order ID", example = "7")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.getOrder(id));
    }

    @Operation(
            summary = "Cancel an order",
            tags = {"Orders"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order was cancelled", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = OrderDTO.class)
            )),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Order was not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(
            @Parameter(description = "Order ID", example = "7")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.cancelOrder(id));
    }

    @Operation(
            summary = "Get order for customer",
            tags = {"Orders"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order was returned", content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            arraySchema = @Schema(description = "List of orders"),
                            schema = @Schema(implementation = OrderDTO.class)
                    )
            )),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")

    })
    @GetMapping("/user/{customerId}")
    public ResponseEntity<Page<OrderDTO>> getOrdersByCustomerId(
            @Parameter(description = "Customer ID", example = "10")
            @PathVariable Long customerId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(service.getOrdersByCustomer(customerId, pageable));
    }
}

