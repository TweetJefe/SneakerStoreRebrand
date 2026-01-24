package com.sneaker.store.cart.controller;

import com.sneaker.store.cart.dto.CartDTO;
import com.sneaker.store.cart.dto.ProductDTO;
import com.sneaker.store.cart.service.CartService;
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

@Tag(name = "Cart", description = "API for work with carts")
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService service;

    @Operation(
            summary = "Create a cart for users",
            tags = {"Cart"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cart was created", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Long.class)
            )),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/create")
    public ResponseEntity<Long> createCart(){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createCart());
    }

    @Operation(
            summary = "Get cart by ID",
            tags = {"Cart"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart was returned", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CartDTO.class)
            )),
            @ApiResponse(responseCode = "404", description = "Cart was not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/show/{cart_id}")
    public ResponseEntity<CartDTO> getCart(
            @Parameter(description = "Cart ID", example = "1")
            @PathVariable Long cart_id
    ) {
        return ResponseEntity.ok(service.getCart(cart_id));
    }


    @Operation(
            summary = "Add product to cart",
            tags = {"Cart"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product was added"),
            @ApiResponse(responseCode = "404", description = "Cart was not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/add/cart/{cartId}/size/{size}")
    public ResponseEntity<Void> addToCart(
            @Parameter(description = "Cart ID", example = "11")
            @PathVariable Long cartId,
            @Parameter(description = "Product size", example = "42")
            @PathVariable Double size,
            @RequestBody ProductDTO dto
    ){
        service.addToCart(cartId, dto, size);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Delete product from cart",
            tags = {"Cart"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product was deleted"),
            @ApiResponse(responseCode = "404", description = "Cart was not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/delete/cart/{cartId}/products/{productId}/size/{size}")
    public ResponseEntity<Void> deleteFromCart(
            @Parameter(description = "Cart ID", example = "11")
            @PathVariable Long cartId,
            @Parameter(description = "Product ID", example = "27")
            @PathVariable Long productId,
            @Parameter(description = "Product size", example = "42")
            @PathVariable Double size
    ){
        service.deleteFromCart(cartId, productId, size);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Increase product in cart",
            tags = {"Cart"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product was increased"),
            @ApiResponse(responseCode = "404", description = "Cart was not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/increase/cart/{cartId}/products/{productId}/size/{size}")
    public ResponseEntity<Void> increaseProduct(
            @Parameter(description = "Cart ID", example = "11")
            @PathVariable Long cartId,
            @Parameter(description = "Product ID", example = "27")
            @PathVariable Long productId,
            @Parameter(description = "Product size", example = "42")
            @PathVariable Double size
    ){
        service.increaseQuantity(cartId, productId, size);
        return ResponseEntity.ok().build();
    }

    @Operation(
                summary = "Decrease product in cart",
                tags = {"Cart"}
        )
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Product was decreased"),
                @ApiResponse(responseCode = "404", description = "Cart was not found"),
                @ApiResponse(responseCode = "500", description = "Internal server error")
        })
    @PatchMapping("/decrease/cart/{cartId}/products/{productId}/size/{size}")
    public ResponseEntity<Void> decreaseProduct(
            @Parameter(description = "Cart ID", example = "11")
            @PathVariable Long cartId,
            @Parameter(description = "Product ID", example = "27")
            @PathVariable Long productId,
            @Parameter(description = "Product size", example = "42")
            @PathVariable Double size
    ){
        service.decreaseQuantity(cartId, productId, size);
        return ResponseEntity.ok().build();
    }

}
